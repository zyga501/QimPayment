package com.weixin.api;

import com.weixin.api.RequestData.UnifiedOrderRequestData;

import java.util.Map;

public class UnifiedOrder extends WeixinAPI {
    public final static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public UnifiedOrder(UnifiedOrderRequestData unifiedOrderRequestData) {
        requestData_ = unifiedOrderRequestData;
    }

    public String getCodeUrl() {
        return code_url;
    }

    public String getPrepayID() {
        return prepay_id;
    }

    @Override
    protected String getAPIUri() {
        return UNIFIEDORDER_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        switch (responseResult.get("trade_type").toString()) {
            case "NATIVE": {
                code_url = responseResult.get("code_url").toString();
                break;
            }
            case "JSAPI": {
                prepay_id = responseResult.get("prepay_id").toString();
            }
        }

        return true;
    }

    private String code_url;
    private String prepay_id;
}
