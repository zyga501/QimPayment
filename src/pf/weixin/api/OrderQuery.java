package pf.weixin.api;

import pf.weixin.api.RequestBean.OrderQueryData;

import java.util.Map;

public class OrderQuery extends WeixinAPIWithSign {
    public final static String ORDERQUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    public OrderQuery(OrderQueryData orderQueryData) {
        requestData_ = orderQueryData;
    }

    @Override
    protected String getAPIUri() {
        return ORDERQUERY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) throws Exception {
        String returnCode = responseResult.get("return_code").toString().toUpperCase();
        String resultCode = responseResult.get("result_code").toString().toUpperCase();
        if (returnCode.compareTo("SUCCESS") == 0) {
            if (resultCode.compareTo("SUCCESS") == 0) {
                if (responseResult.get("trade_state").toString().toUpperCase().compareTo("USERPAYING") == 0) {
                    Thread.sleep(10000);
                    return postRequest(apiKey_);
                }

                return true;
            }
            else {
                String errorCode = responseResult.get("err_code").toString().toUpperCase();
                if (errorCode.compareTo("SYSTEMERROR") == 0) {
                    return postRequest(apiKey_);
                }
            }
        }
        return false;
    }
}
