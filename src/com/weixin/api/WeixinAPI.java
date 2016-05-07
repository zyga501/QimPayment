package com.weixin.api;

import com.framework.utils.HttpClient;

public abstract class WeixinAPI extends HttpClient {
    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}
