package com.weixin.action;

import com.framework.action.AjaxActionSupport;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";

    public void scanPay() {
        System.out.print("scanPay Callback");
    }
}
