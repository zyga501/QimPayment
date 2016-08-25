package com.weixin.api;

import com.framework.utils.Logger;
import net.sf.json.JSONObject;

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
    protected boolean parseResponse(String... args) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(args[0]);
        if (jsonParse.get("errcode") != null) {
            String errorCode = jsonParse.get("errcode").toString();
            switch (errorCode) {
                case "0": {
                    return true;
                }
                case "40001": {
                    String appid = AccessToken.getAppidByAccessToken(accessToken_);
                    if (appid.isEmpty()) {
                        Logger.error("AccessToken Handler Error!");
                        return false;
                    }

                    accessToken_ = AccessToken.updateAccessToken(appid, accessToken_);
                    return postRequest(postData_);
                }
                default: {
                    Logger.error("UnHandler Exception!");
                    Logger.error("Request Url:\r\n" + getAPIUri() + "\r\nRequest Data:\r\n" + postData_ + "\r\nResponse Data:\r\n" + args[0]);
                    return false;
                }
            }
        }
        return super.parseResponse(args);
    }

    private String accessToken_;
    protected String postData_;
}
