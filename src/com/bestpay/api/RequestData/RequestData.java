package com.bestpay.api.RequestData;

public class RequestData {
    public RequestData() {
        channel = "05";
        busiType = "0001";
    }

    public boolean checkParameter() {
        try {
            return !merchantId.isEmpty()
                    && !channel.isEmpty()
                    && !busiType.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildMac(String keyString) {

    }

    public String merchantId; // 商户号
    public String channel; // 渠道
    public String busiType; // 业务类型
    public String mac; // MAC校验域

    // option
    public String subMerchantId; // 子商户号
}
