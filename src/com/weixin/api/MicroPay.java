package com.weixin.api;

import com.weixin.api.RequestData.MicroPayRequestData;

import java.util.Map;

public class MicroPay extends WeixinAPI {
    public final static String MICROPAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

    public MicroPay(MicroPayRequestData microPayRequestData) {
        requestData_ = microPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return MICROPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        return true;
    }
}
