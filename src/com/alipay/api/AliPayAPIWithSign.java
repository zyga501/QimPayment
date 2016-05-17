package com.alipay.api;

import com.alipay.api.RequestData.RequestData;
import com.alipay.utils.Signature;
import com.framework.utils.ClassUtils;
import com.framework.utils.HttpUtils;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.Map;

public abstract class AliPayAPIWithSign extends AliPayAPI {
    public boolean postRequest(String privateKey, String publicKey) throws Exception {
        if (!requestData_.checkParameter() || privateKey.isEmpty()) {
            Logger.warn(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.biz_content = requestData_.buildRequestData();
        RequestData buildObject = new RequestData(requestData_);
        buildObject.sign = requestData_.sign = Signature.generateSign(buildObject, privateKey);

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        buildObject.biz_content = null;
        apiUri += "?" + ClassUtils.convertToQuery(buildObject, "utf-8", false);
        Logger.debug("Request Url:\r\n" + apiUri);

        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new StringEntity("biz_content=" + URLEncoder.encode(requestData_.biz_content, "utf-8"), "UTF-8");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("Accept", "text/xml,text/javascript,text/html");
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
