package pf.jdpay.api;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import framework.utils.HttpUtils;
import framework.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.jdpay.api.RequestBean.RequestData;
import pf.jdpay.utils.Signature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JDAPIWithSign extends JDAPI {
    public Map<String, Object> getResponseResult() {
        return responseResult_;
    }

    public boolean postRequest(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            ProjectLogger.warn(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.buildSign(apiKey);

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        ProjectLogger.debug("Request Url:\r\n" + apiUri);

        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(requestData_);
        ProjectLogger.debug("Reqest Data:\r\n" + postDataXML);

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list = Signature.mapToNameValuePairList(requestData_);
        HttpPost httpPost = new HttpPost(apiUri);
        HttpEntity postEntity = new UrlEncodedFormEntity(list, "utf-8");
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

        boolean ret = parseResponse(responseString) && handlerResponse(responseResult_);
        if (!ret) {
            ProjectLogger.error("Request Url:\r\n" + apiUri);
            ProjectLogger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }

    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        responseResult_ = JsonUtils.toMap(responseString,true);
        return true;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    protected Map<String, Object> responseResult_;
}