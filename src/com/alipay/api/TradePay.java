package com.alipay.api;

public class TradePay extends AliPayAPI {
    public final static String TRADEPAY_API = "https://openapi.alipay.com/gateway.do";

    @Override
    protected String getAPIUri() {
        return TRADEPAY_API;
    }
}
