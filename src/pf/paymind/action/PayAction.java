package pf.paymind.action;

import QimCommon.struts.AjaxActionSupport;
import pf.paymind.api.JsPay;
import pf.paymind.api.RequestBean.JsPayRequestData;

public class PayAction  {
    public static void main(String[] args) throws Exception {
        JsPayRequestData jsPayRequestData = new JsPayRequestData();
        jsPayRequestData.amount = 0.01;
        jsPayRequestData.merchantNo = "B100001354";
        JsPay jsPay = new JsPay(jsPayRequestData);
        jsPay.postRequest("yyapMNP2yTiIW9B7e9uxVeu48chvZGzk");
    }

    public void jsPay() throws Exception {
        JsPayRequestData jsPayRequestData = new JsPayRequestData();
        jsPayRequestData.amount = 0.01;
        jsPayRequestData.merchantNo = "B100001354";
        JsPay jsPay = new JsPay(jsPayRequestData);
        jsPay.postRequest("yyapMNP2yTiIW9B7e9uxVeu48chvZGzk");
    }
}
