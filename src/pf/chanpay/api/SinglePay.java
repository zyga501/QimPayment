package pf.chanpay.api;

import pf.chanpay.api.RequestBean.SinglePayRequestData;

public class SinglePay extends ChanPayAPIWithSign {
    public SinglePay(SinglePayRequestData requestData) {
        requestData_ = requestData;
    }
}