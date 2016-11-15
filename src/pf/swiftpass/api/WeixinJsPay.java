package pf.swiftpass.api;

import pf.swiftpass.api.RequestBean.WeixinJsPayRequestData;

import java.util.Map;

public class WeixinJsPay extends SwiftPassAPIWithSign {
    public final static String JSPAY_API = "https://pay.swiftpass.cn/pay/gateway";
    public final static String JSPAY_REDIRECT = "https://pay.swiftpass.cn/pay/jspay?token_id=%s";

    public WeixinJsPay(WeixinJsPayRequestData weixinJsPayRequestData) {
        requestData_ = weixinJsPayRequestData;
    }

    public String getRedirectUrl() {
        return redirectUrl_;
    }

    @Override
    protected String getAPIUri() {
        return JSPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        if (responseResult.get("token_id") != null) {
            redirectUrl_ = String.format(JSPAY_REDIRECT, responseResult.get("token_id").toString());
            return true;
        }
        return false;
    }

    private String redirectUrl_;
}
