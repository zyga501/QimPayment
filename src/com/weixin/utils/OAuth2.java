package com.weixin.utils;

import com.framework.utils.HttpUtils;
import net.sf.json.JSONObject;

import java.io.IOException;

public class OAuth2 {
    private final static String OPENID_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private final static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public static String fetchOpenid(String appid, String appSecret , String code) throws IOException {
        try {
            String openidUrl = String.format(OPENID_API, appid, appSecret, code);
            JSONObject jsonParse = JSONObject.fromObject(HttpUtils.Get(openidUrl));
            return jsonParse.get("openid").toString();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public static String fetchAccessToken(String appid, String appSecret) throws IOException {
        String accessTokenUrl = String.format(ACCESS_TOKEN, appid, appSecret);
        JSONObject jsonParse = JSONObject.fromObject(HttpUtils.Get(accessTokenUrl));
        return jsonParse.get("access_token").toString();
    }
}
