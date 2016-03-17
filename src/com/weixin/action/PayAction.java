package com.weixin.action;

import com.framework.action.AjaxActionSupport;

public class PayAction extends AjaxActionSupport {
    public String microPay() {
        return AjaxActionComplete();
    }
}
