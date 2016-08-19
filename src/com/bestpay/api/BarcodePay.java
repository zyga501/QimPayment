package com.bestpay.api;

import com.bestpay.api.RequestData.BarcodePayRequestData;

import java.util.Map;

public class BarcodePay extends BestPayWithSign {
    public final static String BARCODEPAY_API = "https://webpaywg.bestpay.com.cn/barcode/placeOrder";

    public BarcodePay(BarcodePayRequestData requestData) {
        requestData_ = requestData;
    }

    @Override
    protected String getAPIUri() {
        return BARCODEPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        if (responseResult.containsKey("result")) {
            Map<String, Object> payResult = (Map<String, Object>)responseResult.get("result");
            if (payResult != null) {
                return payResult.get("transStatus").toString().compareTo("B") == 0;
            }
        }

        return false;
    }

}
