package com.weixin.action;

import com.framework.action.AjaxActionSupport;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";

    public void scanPay() {
        System.out.print("scanPay Callback\n");
    }

    public void brandWCPay() {
        System.out.print("brandWCPay Callback\n");
    }
}
