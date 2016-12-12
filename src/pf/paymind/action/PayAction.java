package pf.paymind.action;

import QimCommon.struts.AjaxActionSupport;
import pf.paymind.api.JsPay;
import pf.paymind.api.RequestBean.JsPayRequestData;
import pf.paymind.action.CallbackAction;

public class PayAction extends AjaxActionSupport {
    public void jsPay() throws Exception {
        JsPayRequestData jsPayRequestData = new JsPayRequestData();
        jsPayRequestData.amount = 0.01;
        jsPayRequestData.merchantNo = "B100001358";
        String requestUrl = getRequest().getRequestURL().toString();
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
        requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "paymind/"
        + CallbackAction.JSPAYCALLBACK;
        jsPayRequestData.serverCallbackUrl = requestUrl;
        JsPay jsPay = new JsPay(jsPayRequestData);
        if (jsPay.postRequest("XrsYj8t2jdcbrljK3Oc7KqfuJtASxKkE")) {
            getResponse().sendRedirect(jsPay.getPayUrl());
        }
    }
}
