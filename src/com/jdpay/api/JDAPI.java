package com.jdpay.api;

import com.framework.utils.HttpClient;

public abstract class JDAPI extends HttpClient {
    @Override
    protected boolean handlerResponse(String... args) throws Exception {
        return true;
    }
}