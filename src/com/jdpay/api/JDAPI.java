package com.jdpay.api;

import com.framework.utils.HttpClient;

public abstract class JDAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String... args) throws Exception {
        return true;
    }
}