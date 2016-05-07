package com.framework.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

public abstract class HttpClient {
    public boolean getRequest() throws Exception {
        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        Logger.debug("Request Url:\r\n" + apiUri);

        String responseString = HttpUtils.GetRequest(new HttpGet(apiUri), (HttpEntity httpEntity) -> {
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
            responseString = HttpUtils.PostRequest(httpPost, (HttpEntity httpEntity) -> {
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

    protected abstract boolean handlerResponse(String responseResult) throws Exception;
}
