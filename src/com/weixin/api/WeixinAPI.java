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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.SocketTimeoutException;
import java.util.Map;

public abstract class WeixinAPI {
    public Map<String,Object> getResponseResult() {
        return responseResult_;
    }

    public boolean execute(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            System.out.println("CheckParameter Failed!");
            return false;
        }

        String sign = Signature.generateSign(requestData_, apiKey);
        requestData_.sign = sign;

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
            CloseableHttpClient httpClient = HttpUtils.Instance();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
            response.close();
        }
        catch (ConnectionPoolTimeoutException e) {
        }
        catch (ConnectTimeoutException e) {
        }
        catch (SocketTimeoutException e) {
        }
        catch (Exception e) {
        }
        finally {
            httpPost.abort();
        }

        System.out.println(responseString);

        responseResult_ = XMLParser.convertMapFromXML(responseString);
        if (!Signature.checkSignValid(responseResult_, apiKey)) {
            System.out.println("checkSignValid Failed!");
            return false;
        }

        return handlerResponse(responseResult_, apiKey);
    }

    protected abstract String getAPIUri();
    protected abstract boolean handlerResponse(Map<String,Object> responseResult, String apiKey) throws Exception;

    protected RequestData requestData_;
    protected Map<String,Object> responseResult_;
}