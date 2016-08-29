package pf.weixin.api;

import pf.weixin.api.RequestData.RefundRequestData;

public class Refund extends WeixinAPIWithSign  {
    public final static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public Refund(RefundRequestData refundRequestData) {
        requestData_ = refundRequestData;
    }
    @Override
    protected String getAPIUri() {
        return REFUND_API;
    }
}
