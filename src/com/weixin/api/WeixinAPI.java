package com.weixin.api;

import com.framework.utils.HttpClient;

public abstract class WeixinAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String... args) throws Exception {
        return true;
    }
}
