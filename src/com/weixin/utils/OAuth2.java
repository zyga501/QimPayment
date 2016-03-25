package com.weixin.utils;

import com.framework.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class OAuth2 {
    private final static String OPENID_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    public static String fetchOpenid(String appid, String appSecret , String code) throws IOException {
        try {
            String accessTokenUrl = String.format(OPENID_API, appid, appSecret, code);
            CloseableHttpClient httpClient = HttpUtils.Instance();
            HttpGet httpGet = new HttpGet(accessTokenUrl);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            response.close();
            JSONObject jsonParse = JSONObject.fromObject(responseString);
            return jsonParse.get("openid").toString();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }
}
