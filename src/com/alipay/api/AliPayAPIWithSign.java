package com.alipay.api;

import com.alipay.api.RequestData.RequestData;
import com.alipay.utils.Signature;
import com.framework.utils.ClassUtils;
import com.framework.utils.HttpUtils;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public abstract class AliPayAPIWithSign extends AliPayAPI {
    public boolean postRequest(String privateKey, String publicKey) throws Exception {
        if (!requestData_.checkParameter() || privateKey.isEmpty()) {
            Logger.warn(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.biz_content = requestData_.buildRequestData();
        requestData_.sign = Signature.generateSign(new RequestData(requestData_), privateKey);

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        apiUri.concat("?" + ClassUtils.convertToQuery(new RequestData(requestData_), "utf-8", false));
        Logger.debug("Request Url:\r\n" + apiUri);

        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new StringEntity(ClassUtils.convertToQuery(requestData_, "utf-8", false), "UTF-8");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("Accept", "text/xml,text/javascript,text/html");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpEntity httpEntity) -> {
                return EntityUtils.toString(httpEntity, "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        Logger.debug("Response Data:\r\n" + responseString);

        responseResult_ = StringUtils.jsonToMap(responseString);

        String rootNode = requestData_.method.replace('.', '_') + "_response";
        if (responseResult_.containsKey("sign") && responseResult_.containsKey(rootNode)) {
            if (!Signature.rsaVerify(StringUtils.jsonToMap(responseResult_.get(rootNode).toString()),
                    responseResult_.get("sign").toString(),
                    publicKey))
                return false;
        }

        return handlerResponse(responseResult_);
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }
    protected RequestData requestData_;
    protected Map<String, Object> responseResult_;
}
