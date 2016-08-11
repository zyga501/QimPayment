package com.bestpay.api;

public class BarcodePay extends BestPayWithSign {
    public final static String BARCODEPAY_API = "https://webpaywg.bestpay.com.cn/barcode/placeOrder";

    @Override
    protected String getAPIUri() {
        return BARCODEPAY_API;
    }
}
