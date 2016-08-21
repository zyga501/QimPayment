package com.bestpay.api;

import com.bestpay.api.RequestData.OrderPayRequestData;

public class OrderPay extends BestPayWithSign {
    public final static String ORDERPAY_API = "https://webpaywg.bestpay.com.cn/order.action";

    public OrderPay(OrderPayRequestData requestData) {
        requestData_ = requestData;
    }

    @Override
    protected String getAPIUri() {
        return ORDERPAY_API;
    }
}
