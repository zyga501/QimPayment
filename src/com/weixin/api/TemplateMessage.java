package com.weixin.api;

public class TemplateMessage extends WeixinAPI {
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    public TemplateMessage(String accessToken) {
        accessToken_ = accessToken;
    }

    @Override
    protected String getAPIUri() {
        return String.format(SEND_MESSAGE, accessToken_);
    }

    @Override
    protected boolean handlerResponse(String responseResult) throws Exception {
        return super.handlerResponse(responseResult);
    }

    private String accessToken_;
}
