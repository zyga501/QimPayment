package com.weixin.api;

import net.sf.json.JSONObject;

public class UserInfo extends WeixinAPI {
    private final static String USERINFO_API = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s";

    public UserInfo(String accessToken, String openid) {
        accessToken_ = accessToken;
        openid_ = openid;
    }

    public String getNickname() {
        return nickname_;
    }

    @Override
    protected String getAPIUri() {
        return String.format(USERINFO_API, accessToken_, openid_);
    }

    @Override
    protected boolean parseResponse(String... args) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(args[0]);
        if (jsonParse.get("nickname") != null) {
            nickname_ = jsonParse.get("nickname").toString();
            return true;
        }
        return false;
    }

    private String accessToken_;
    private String openid_;
    private String nickname_;
}
