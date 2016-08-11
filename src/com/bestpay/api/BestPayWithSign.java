package com.bestpay.api;

import com.framework.utils.HttpUtils;
import com.framework.utils.Logger;
import com.framework.utils.XMLParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.bestpay.api.RequestData.RequestData;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public abstract class BestPayWithSign extends BestPayAPI {
    public boolean postRequest(String keyString) throws Exception {
        if (!requestData_.checkParameter() || keyString.isEmpty()) {
            Logger.error(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.buildMac(keyString);

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

        boolean ret = false;
        XMLParser.convertMapFromXML(responseString);

        if (!ret) {
            Logger.error("Request Url:\r\n" + apiUri);
            Logger.error("Reqest Data:\r\n" + postDataXML);
            Logger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }

    protected RequestData requestData_;
}
