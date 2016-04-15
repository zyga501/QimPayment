package com.weixin.api;

import com.framework.utils.Logger;
import com.weixin.database.MerchantInfo;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessToken extends WeixinAPI{
    private final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static Map<String, String> accessTokenMap_ = new HashMap<>();
    public static String getAccessToken(String appid) throws Exception {
        synchronized(accessTokenMap_) {
            if (accessTokenMap_.get(appid) != null) {
                return accessTokenMap_.get(appid);
            }

            updateAccessToken(appid);
            if (accessTokenMap_.get(appid) != null) {
                return accessTokenMap_.get(appid);
            }

            Logger.error("Get Access Token Failed!");
            return null;
        }
    }

    public static void updateAccessToken(String appid) throws Exception {
        MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoByAppId(appid);
        if (merchantInfo != null) {
            AccessToken accessToken = new AccessToken(merchantInfo.getAppid(), merchantInfo.getAppsecret());
            if (accessToken.getRequest()) {
                accessTokenMap_.put(appid, accessToken.getAccessToken());
            }
        }
    }

    public static void updateAccessToken(List<String> appidList) throws Exception {
        synchronized(accessTokenMap_) {
            for (int index = 0; index < appidList.size(); ++index) {
                String appid = appidList.get(index);
                updateAccessToken(appid);
            }
        }
    }

    public AccessToken(String appid, String appSecret) {
        appid_ = appid;
        appSecret_ = appSecret;
    }

    public String getAccessToken() { return accessToken_; }

    @Override
    protected String getAPIUri() {
        return String.format(ACCESS_TOKEN_API, appid_, appSecret_);
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
