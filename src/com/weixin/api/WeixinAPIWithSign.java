package com.weixin.api;

import com.framework.utils.HttpUtils;
import com.framework.utils.Logger;
import com.framework.utils.XMLParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weixin.api.RequestData.RequestData;
import com.weixin.utils.Signature;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public abstract class WeixinAPIWithSign extends WeixinAPI {
    public Map<String, Object> getResponseResult() {
        return responseResult_;
    }

    public boolean postRequest(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            Logger.error(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        String sign = Signature.generateSign(requestData_, apiKey_ = apiKey);
        requestData_.sign = sign;

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }

        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(requestData_);

        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse) -> {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        responseResult_ = XMLParser.convertMapFromXML(responseString);

        boolean ret = true;
        if (!Signature.checkSignValid(responseResult_, apiKey)) {
            Logger.error(this.getClass().getName() + " CheckSignValid Failed!");
            ret = false;
        }

        ret = ret && handlerResponse(responseResult_);

        if (!ret) {
            Logger.error("Request Url:\r\n" + apiUri);
            Logger.error("Reqest Data:\r\n" + postDataXML);
            Logger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    protected String apiKey_;
    protected Map<String, Object> responseResult_;
}
