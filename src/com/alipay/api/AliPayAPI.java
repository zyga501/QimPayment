package com.alipay.api;

import com.framework.utils.HttpClient;

public abstract class AliPayAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String... args) throws Exception {
        return true;
    }
}
