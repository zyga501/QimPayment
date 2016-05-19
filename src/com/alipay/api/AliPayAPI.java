package com.alipay.api;

import com.framework.utils.HttpClient;

public abstract class AliPayAPI extends HttpClient {
    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}
