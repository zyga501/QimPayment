package com.api.mode;

import com.weixin.utils.Signature;

public class WeixinMode extends BaseMode  {
    private final static String WeixinMicroPay = "WeixinMicroPay";
    private final static String WeixinScanPay = "WeixinScanPay";
    private final static String WeixinPrePay = "WeixinPrePay";
    private final static String WeixinBrandWCPay = "WeixinBrandWCPay";
    private final static String WeixinOrderQuery = "WeixinOrderQuery";

    public String microPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.microPay();
        }

        return WeixinMicroPay;
    }

    public String scanPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.scanPay();
        }

        return WeixinScanPay;
    }

    public String prePay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.prePay();
        }

        return WeixinPrePay;
    }

    public String brandWCPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.brandWCPay();
        }

        return WeixinBrandWCPay;
    }

    public String orderQuery() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.orderQuery();
        }

        return WeixinOrderQuery;
    }
}
