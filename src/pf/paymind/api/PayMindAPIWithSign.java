package pf.paymind.api;

import QimCommon.utils.HttpUtils;
import QimCommon.utils.JsonUtils;
import QimCommon.utils.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.paymind.api.RequestBean.RequestData;

import java.util.Map;

abstract public class PayMindAPIWithSign extends PayMindAPI {
    public boolean postRequest(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            ProjectLogger.error(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.buildSign(apiKey);

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }

        String postData = requestData_.buildRequestData();

        HttpPost httpPost = new HttpPost(apiUri + "?" + postData);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse) -> {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        boolean ret = parseResponse(responseString, apiKey) && handlerResponse(responseResult_);
        if (!ret) {
            ProjectLogger.error("Request Url:\r\n" + apiUri);
            ProjectLogger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }

    protected boolean parseResponse(String ...args) throws Exception {
        responseResult_ = JsonUtils.toMap(args[0], true);
        if (StringUtils.convertNullableString(responseResult_.get("retCode")).compareTo("0000") == 0) {
            return true;
        }

        return false;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    private Map<String, Object> responseResult_;
}
