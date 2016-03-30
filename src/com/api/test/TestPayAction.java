package com.api.test;

import com.framework.action.AjaxActionSupport;

public class TestPayAction extends AjaxActionSupport {
    private final static String MicroPay = "ApiMicroPay";
    private final static String ScanPay = "ApiScanPay";
    private final static String PrePay = "ApiPrePay";
    private final static String BrandWCPay = "ApiBrandWCPay";

    public String microPay() {
        return MicroPay;
    }

    public String scanPay() {
        return ScanPay;
    }

    public String prePay() {
        return PrePay;
    }

    public String brandWCPay() {
        return BrandWCPay;
    }
}