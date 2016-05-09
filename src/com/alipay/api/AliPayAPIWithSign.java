package com.alipay.api;

import com.alipay.api.RequestData.RequestData;

import java.util.Map;

public abstract class AliPayAPIWithSign extends AliPayAPI {

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }
    protected RequestData requestData_;
    protected Map<String, Object> responseResult_;
}
