package com.bestpay.action;

import com.framework.action.AjaxActionSupport;

public class PayAction extends AjaxActionSupport {
    public String barcodePay() throws Exception {
        return AjaxActionComplete();
    }
}
