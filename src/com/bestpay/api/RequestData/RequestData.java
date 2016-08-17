package com.bestpay.api.RequestData;

public class RequestData {
    public RequestData() {

    }

    public boolean checkParameter() {
        try {
            return !merchantId.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildMac(String keyString) {

    }

    public String merchantId; // 商户号
    public String mac; // MAC校验域

    // option
    public String subMerchantId; // 子商户号
}
