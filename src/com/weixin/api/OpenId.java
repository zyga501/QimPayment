package com.weixin.api;

import java.util.Map;

public class OpenId extends WeixinAPI {
    private final static String OPENID_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    public OpenId(String appid, String appSecret , String code) {
        appid_ = appid;
        appSecret_ = appSecret;
        code_ = code;
    }

    @Override
    protected String getAPIUri() {
        return String.format(OPENID_API, appid_, appSecret_, code_);
    }

    protected boolean handlerResponse(Map<String,Object> responseResult, String apiKey) throws Exception {
        return false;
    }

    private String appid_;
    private String appSecret_;
    private String code_;
}
