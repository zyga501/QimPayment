package com.alipay.api;

import com.framework.utils.HttpClient;

public abstract class AliPayAPI extends HttpClient {
    @Override
    protected boolean handlerResponse(String... args) throws Exception {
        return true;
    }
}
