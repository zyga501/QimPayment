package pf.chanpay.action;

import pf.chanpay.api.RequestBean.SinglePayRequestData;
import pf.chanpay.api.SinglePay;
import pf.framework.action.AjaxActionSupport;
import pf.framework.base.ProjectSettings;

import java.util.Map;

public class PayAction extends AjaxActionSupport{
    public String singlePay() throws Exception {
        SinglePayRequestData singlePayRequestData = new SinglePayRequestData();
        singlePayRequestData.amount = Integer.parseInt(getParameter("amount").toString());
        singlePayRequestData.bankGeneralName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BANK_GENERAL_NAME").toString();
        singlePayRequestData.accountNo = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("ACCOUNT_NO").toString();
        singlePayRequestData.accountName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("ACCOUNT_NAME").toString();
        singlePayRequestData.bankName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BANK_NAME").toString();
        singlePayRequestData.bankCode = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BANK_CODE").toString();
        SinglePay singlePay = new SinglePay(singlePayRequestData);
        singlePay.postRequest();
        return AjaxActionComplete();
    }
}
