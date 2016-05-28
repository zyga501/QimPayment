package com.jdpay.api;

import com.framework.utils.HttpClient;

public abstract class JDAPI extends HttpClient {
    protected boolean handlerResponse(String responseResult) throws Exception {
        return true;
    }
}