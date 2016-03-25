package com.weixin.api;

import com.weixin.api.RequestData.RefundRequestData;

import java.util.Map;

public class Refund extends WeixinAPI  {
    public final static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public Refund(RefundRequestData refundRequestData) {
        requestData_ = refundRequestData;
    }
    @Override
    protected String getAPIUri() {
        return REFUND_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        return true;
    }
}
