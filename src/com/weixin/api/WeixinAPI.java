package com.weixin.api;

import com.framework.utils.HttpClient;
import com.framework.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public abstract class WeixinAPI {
    public boolean getRequest() throws Exception {
        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        Logger.debug("Request Url:\r\n" + apiUri);

        String responseString = HttpClient.GetRequest(new HttpGet(apiUri), (HttpEntity httpEntity) -> {
            return EntityUtils.toString(httpEntity, "UTF-8");
        });

        Logger.debug("Response Data:\r\n" + responseString);

        return handlerResponse(responseString);
    }

    public boolean postRequest(String postData) throws Exception {
        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        Logger.debug("Request Url:\r\n" + apiUri);

        Logger.debug("Reqest Data:\r\n" + postData);

        HttpPost httpPost = new HttpPost(apiUri);
        StringEntity postEntity = new StringEntity(postData, "UTF-8");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpClient.PostRequest(httpPost, (HttpEntity httpEntity) -> {
                return EntityUtils.toString(httpEntity, "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        Logger.debug("Response Data:\r\n" + responseString);

        return handlerResponse(responseString);
    }

    protected abstract String getAPIUri();

    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}
