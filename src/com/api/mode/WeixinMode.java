package com.api.mode;

import com.weixin.utils.Signature;

public class WeixinMode extends BaseMode  {
    private final static String WeixinMicroPay = "WeixinMicroPay";
    private final static String WeixinScanPay = "WeixinScanPay";
    private final static String WeixinJsPay = "WeixinJsPay";
    private final static String WeixinOrderQuery = "WeixinOrderQuery";
    private final static String WeixinOrderInsert = "WeixinOrderInsert";

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

    public String jsPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.jsPay();
        }

        return WeixinJsPay;
    }

    public String orderQuery() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.orderQuery();
        }

        return WeixinOrderQuery;
    }

    public String orderInsert() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            return super.orderInsert();
        }

        return WeixinOrderInsert;
    }
}
