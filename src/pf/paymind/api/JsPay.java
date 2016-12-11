package pf.paymind.api;

import pf.paymind.api.RequestBean.JsPayRequestData;

public class JsPay extends PayMindAPIWithSign {
    public final static String JSPAY_API = "http://api.izhongyin.com/middlepaytrx/wx/scanCommonCode";

    public JsPay(JsPayRequestData jsPayRequestData) {
        requestData_ = jsPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return JSPAY_API;
    }

}
