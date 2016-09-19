package pf.weixin.api;

import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxSubMerchantInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessToken extends WeixinAPI{
    private final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static Map<String, String> accessTokenMap_ = new HashMap<>();

    public static String getAppidByAccessToken(String accessToken) {
        synchronized(accessTokenMap_) {
            if (accessTokenMap_.containsValue(accessToken)) {
                for (Map.Entry<String, String> entry : accessTokenMap_.entrySet()) {
                    if (entry.getValue().compareTo(accessToken) == 0) {
                        return entry.getKey();
                    }
                }
            }

            return "";
        }
    }

    public static String getAccessToken(String appid) throws Exception {
        synchronized(accessTokenMap_) {
            if (accessTokenMap_.get(appid) != null) {
                return accessTokenMap_.get(appid);
            }

            updateAccessToken(appid);
            if (accessTokenMap_.get(appid) != null) {
                return accessTokenMap_.get(appid);
            }

            ProjectLogger.error("Get Access Token Failed!");
            return null;
        }
    }

    public static void updateAccessToken(String appid) throws Exception {
        synchronized(accessTokenMap_) {
            String appsecret = new String();
            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoByAppId(appid);
            if (merchantInfo != null) {
                appsecret = merchantInfo.getAppsecret();
            }
            else {
                WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoByAppId(appid);
                if (subMerchantInfo != null) {
                    appsecret = subMerchantInfo.getAppsecret();
                }
            }

            if (!appsecret.isEmpty()) {
                AccessToken accessToken = new AccessToken(appid, appsecret);
                if (accessToken.getRequest()) {
                    accessTokenMap_.put(appid, accessToken.getAccessToken());
                }
            }
        }
    }

    public static String updateAccessToken(String appid, String invalidAccessToken) throws Exception {
        synchronized(accessTokenMap_) {
            if (!accessTokenMap_.containsValue(invalidAccessToken)) {
                return getAccessToken(appid);
            }

            updateAccessToken(appid);
            return getAccessToken(appid);
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
    protected boolean parseResponse(String responseString) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(responseString);
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
