package pf.bestpay.api;

import QimCommon.utils.HttpUtils;
import QimCommon.utils.JsonUtils;
import QimCommon.utils.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.bestpay.api.RequestBean.RequestData;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class BestPayAPIWithSign extends BestPayAPI {
    public boolean postRequest(String keyString) throws Exception {
        if (!requestData_.checkParameter() || keyString.isEmpty()) {
            ProjectLogger.error(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.buildMac(keyString);

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }

        HttpPost httpPost = new HttpPost(apiUri);
        httpPost.setEntity(buildPostStringEntity());

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

    protected StringEntity buildPostStringEntity() throws UnsupportedEncodingException {
        return new StringEntity("");
    }

    protected boolean parseResponse(String responseString) throws Exception {
        responseResult_ = JsonUtils.toMap(responseString, true);
        return StringUtils.convertNullableString(responseResult_.get("success")).compareTo("true") == 0;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    private Map<String, Object> responseResult_;
}
