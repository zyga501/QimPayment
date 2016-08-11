package com.bestpay.api;

import com.framework.utils.HttpClient;

public abstract class BestPayAPI extends HttpClient {
    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}
