package com.weixin.api;

import com.weixin.api.RequestData.MicroPayRequestData;

public class MicroPay extends WeixinAPI {
    public static String MICROPAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

    public MicroPay(MicroPayRequestData microPayInfo) {
        requestData_ = microPayInfo;
    }

    @Override
    protected String getAPIUri() {
        return MICROPAY_API;
    }
}
