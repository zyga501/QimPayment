package com.weixin.api;

import com.framework.utils.HttpUtils;
import com.framework.utils.XMLParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weixin.api.RequestData.RequestData;
import com.weixin.utils.Signature;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.SocketTimeoutException;
import java.util.Map;

public abstract class WeixinAPI {
    public boolean getRequest() throws Exception {
        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }

        CloseableHttpClient httpClient = HttpUtils.Instance();
        HttpGet httpGet = new HttpGet(apiUri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        response.close();

        return handlerResponse(responseString);
    }

    protected abstract String getAPIUri();

    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}
