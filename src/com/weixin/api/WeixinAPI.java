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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

public abstract class WeixinAPI {
    public Map<String,Object> responseResult() {
        return responseResult_;
    }

    public boolean execute(String appsecret) throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        if (!requestData_.checkParameter() || appsecret.isEmpty()) {
            return false;
        }

        String sign = Signature.generateSign(requestData_.convertToMap(), appsecret);
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

        if (!Signature.checkIsSignValidFromResponseString(responseString, appsecret)) {
            return false;
        }

        responseResult_ = XMLParser.convertMapFromXML(responseString);
        return true;
    }

    protected abstract String getAPIUri();

    protected RequestData requestData_;
    private Map<String,Object> responseResult_;
}
