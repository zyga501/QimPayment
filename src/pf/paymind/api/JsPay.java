package pf.paymind.api;

import pf.paymind.api.RequestBean.JsPayRequestData;

import java.util.Map;

public class JsPay extends PayMindAPIWithSign {
    public final static String JSPAY_API = "http://real.izhongyin.com/middlepaytrx/wx/scanCommonCode";

    public JsPay(JsPayRequestData jsPayRequestData) {
        requestData_ = jsPayRequestData;
    }

    public String getPayUrl() {
        return payUrl_;
    }

    @Override
    protected String getAPIUri() {
        return JSPAY_API;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        payUrl_ = responseResult.get("qrCode").toString();
        return true;
    }

    private String payUrl_;
}
