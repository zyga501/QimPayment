package pf.chanpay.action;

import pf.chanpay.api.RequestBean.OneTimeRequestData;
import pf.framework.action.AjaxActionSupport;

public class PayAction extends AjaxActionSupport{
    public String OneTimePay() throws Exception {
        OneTimeRequestData oneTimeRequestData = new OneTimeRequestData();
        return AjaxActionComplete();
    }
}
