package com.bestpay.api;

import com.framework.utils.HttpClient;

public abstract class BestPayAPI extends HttpClient {
    @Override
    protected boolean handlerResponse(String... args) throws Exception {
        return true;
    }
}
