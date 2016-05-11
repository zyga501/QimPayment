package com.alipay.api;

import com.alipay.api.RequestData.RequestData;
import com.alipay.utils.Signature;
import com.framework.utils.Logger;

import java.util.Map;

public abstract class AliPayAPIWithSign extends AliPayAPI {
    @Override
    public boolean postRequest(String privateKey) throws Exception {
        if (!requestData_.checkParameter() || privateKey.isEmpty()) {
            Logger.warn(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        requestData_.biz_content = requestData_.buildRequestData();
        requestData_.sign = Signature.generateSign(new RequestData(requestData_), privateKey);

        return false;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }
    protected RequestData requestData_;
    protected Map<String, Object> responseResult_;
}
