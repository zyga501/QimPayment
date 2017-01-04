package pf.weixin.api;

import net.sf.json.JSONObject;
import pf.ProjectLogger;

public class TemplateMessage extends WeixinAPI {
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    public TemplateMessage(String accessToken) {
        accessToken_ = accessToken;
    }

    @Override
    public boolean postRequest(String postData) throws Exception {
        postData_ = postData;
        return super.postRequest(postData);
    }

    @Override
    protected String getAPIUri() {
        return String.format(SEND_MESSAGE, accessToken_);
    }

    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(responseString);
        if (jsonParse.get("errcode") != null) {
            String errorCode = jsonParse.get("errcode").toString();
            switch (errorCode) {
                case "0": {
                    return true;
                }
                case "40001": {
                    String appid = AccessToken.getAppidByAccessToken(accessToken_);
                    if (appid.isEmpty()) {
                        ProjectLogger.error("AccessToken Handler Error!");
                        return false;
                    }

                    accessToken_ = AccessToken.updateAccessToken(appid, accessToken_);
                    return postRequest(postData_);
                }
                // "errcode":40003,"errmsg":"invalid openid hint: [GgDwrA0975rsz6]"
                case "40003":
                // "errcode":43004,"errmsg":"require subscribe hint: [_Mdu0a0124rsz4]"
                case "43004": {
                    return true;
                }
                default: {
                    ProjectLogger.error("UnHandler Exception!");
                    ProjectLogger.error("Request Url:\r\n" + getAPIUri() + "\r\nRequest Data:\r\n" + postData_ + "\r\nResponse Data:\r\n" + responseString);
                    return false;
                }
            }
        }
        return super.parseResponse(responseString);
    }

    private String accessToken_;
    protected String postData_;
}
