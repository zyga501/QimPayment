package com.weixin.api;

import com.weixin.api.RequestData.OrderQueryData;

import java.util.Map;

public class OrderQuery extends WeixinAPI {
    public final static String ORDERQUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    public OrderQuery(OrderQueryData orderQueryData) {
        requestData_ = orderQueryData;
    }

    @Override
    protected String getAPIUri() {
        return ORDERQUERY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult, String appsecret) throws Exception {
        String returnCode = responseResult.get("return_code").toString().toUpperCase();
        String resultCode = responseResult.get("result_code").toString().toUpperCase();
        if (returnCode.compareTo("SUCCESS") == 0) {
            if (resultCode.compareTo("SUCCESS") == 0) {
                if (responseResult.get("trade_state").toString().toUpperCase().compareTo("USERPAYING") == 0) {
                    Thread.sleep(10000);
                    return execute(appsecret);
                }
                return true;
            }
            else {
                String errorCode = responseResult.get("err_code").toString().toUpperCase();
                if (errorCode.compareTo("SYSTEMERROR") == 0) {
                    return execute(appsecret);
                }
            }
        }
        return false;
    }
}
