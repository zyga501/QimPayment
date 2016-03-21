package com.weixin.api;

import com.weixin.api.RequestData.UnifiedOrderRequestData;

import java.util.Map;

public class UnifiedOrder extends WeixinAPI {
    public final static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public UnifiedOrder(UnifiedOrderRequestData unifiedOrderRequestData) {
        requestData_ = unifiedOrderRequestData;
    }

    public String getCodeUrl() { return code_url; }
    @Override
    protected String getAPIUri() {
        return UNIFIEDORDER_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        if (((UnifiedOrderRequestData)requestData_).trade_type.compareTo("NATIVE") == 0) {
            code_url = responseResult.get("code_url").toString();
        }
        return true;
    }

    private String code_url;
}
