package com.api.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;

public class PayAction extends AjaxActionSupport {
    private final static String WeixinMode = "weixin";

    private final static String WeixinMicroPay = "WeixinMicroPay";
    private final static String WeixinScanPay = "WeixinScanPay";
    private final static String WeixinPrePay = "WeixinPrePay";
    private final static String WeixinBrandWCPay = "WeixinBrandWCPay";

    public String microPay() {
        switch (getMode()) {
            case WeixinMode:
                return WeixinMicroPay;
            default:
                return WeixinMicroPay;
        }
    }

    public String scanPay() {
        switch (getMode()) {
            case WeixinMode:
                return WeixinScanPay;
            default:
                return WeixinScanPay;
        }
    }

    public String prePay() {
        switch (getMode()) {
            case WeixinMode:
                return WeixinPrePay;
            default:
                return WeixinPrePay;
        }
    }

    public String brandWCPay() {
        switch (getMode()) {
            case WeixinMode:
                return WeixinBrandWCPay;
            default:
                return WeixinBrandWCPay;
        }
    }

    private String getMode() {
        return StringUtils.convertNullableString(getParameter("mode"), WeixinMode);
    }
}
