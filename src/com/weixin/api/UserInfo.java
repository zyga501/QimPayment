package com.weixin.api;

import net.sf.json.JSONObject;

public class UserInfo extends WeixinAPI {
    private final static String USERINFO_API = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s";

    public UserInfo(String appid, String openid) {
        appid_ = appid;
        openid_ = openid;
    }

    public String getNickname() {
        return nickname_;
    }

    @Override
    protected String getAPIUri() {
        return String.format(USERINFO_API, appid_, openid_);
    }

    @Override
    protected boolean handlerResponse(String responseResult) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(responseResult);
        if (jsonParse.get("nickname") != null) {
            nickname_ = jsonParse.get("nickname").toString();
            return true;
        }
        return false;
    }

    private String appid_;
    private String openid_;
    private String nickname_;
}
