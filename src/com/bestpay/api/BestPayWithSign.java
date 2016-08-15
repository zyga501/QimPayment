package com.bestpay.api;

import com.bestpay.api.RequestData.RequestData;
import com.framework.utils.*;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Map;

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

        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new UrlEncodedFormEntity(ClassUtils.ConvertToList(requestData_, true), Consts.UTF_8);
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

        responseResult_ = JsonUtils.toMap(responseString, true);
        boolean ret = StringUtils.convertNullableString(responseResult_.get("success")).compareTo("true") == 0;
        ret = ret && handlerResponse(responseResult_);

        if (!ret) {
            Logger.error("Request Url:\r\n" + apiUri);
            Logger.error("Response Data:\r\n" + responseString);
        }

        return ret;
    }


    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    protected Map<String, Object> responseResult_;
}
