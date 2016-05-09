package com.alipay.api;

import com.alipay.api.RequestData.TradePayRequestData;

public class TradePay extends AliPayAPIWithSign {
    public final static String TRADEPAY_API = "https://openapi.alipay.com/gateway.do";

    public TradePay(TradePayRequestData requestData) {
        requestData_ = requestData;
        requestData_.method = "alipay.trade.pay";
    }

    @Override
    protected String getAPIUri() {
        return TRADEPAY_API;
    }
}
