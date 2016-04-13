package com.weixin.api;

import net.sf.json.JSONObject;

public class AccessToken extends WeixinAPI{
    private final static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public AccessToken(String appid, String appSecret) {
        appid_ = appid;
        appSecret_ = appSecret;
    }

    public String getAccessToken() { return accessToken_; }

    @Override
    protected String getAPIUri() {
        return String.format(ACCESS_TOKEN, appid_, appSecret_);
    }

    @Override
    protected boolean handlerResponse(String responseResult) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(responseResult);
        if (jsonParse.get("access_token") != null) {
            accessToken_ = jsonParse.get("access_token").toString();
            return true;
        }
        return false;
    }

    private String appid_;
    private String appSecret_;
    private String accessToken_;
}
