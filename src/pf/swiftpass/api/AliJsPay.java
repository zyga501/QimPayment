package pf.swiftpass.api;

import pf.swiftpass.api.RequestBean.AliJsPayRequestData;

import java.util.Map;

public class AliJsPay extends SwiftPassAPIWithSign {
    public final static String JSPAY_API = "https://pay.swiftpass.cn/pay/gateway";

    public AliJsPay(AliJsPayRequestData aliJsPayRequestData) {
        requestData_ = aliJsPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return JSPAY_API;
    }

    public String getPayUrl() {
        return payUrl_;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        if (responseResult.containsKey("pay_url")) {
            payUrl_ = responseResult.get("pay_url").toString();
            return true;
        }
        return false;
    }

    private String payUrl_;
}
