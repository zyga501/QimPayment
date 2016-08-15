package com.bestpay.api;

import com.bestpay.api.RequestData.BarcodePayRequestData;

public class BarcodePay extends BestPayWithSign {
    public final static String BARCODEPAY_API = "https://webpaywg.bestpay.com.cn/barcode/placeOrder";

    public BarcodePay(BarcodePayRequestData requestData) {
        requestData_ = requestData;
    }

    @Override
    protected String getAPIUri() {
        return BARCODEPAY_API;
    }
}
