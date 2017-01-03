package pf.paymind.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.SessionCache;
import QimCommon.utils.StringUtils;
import QimCommon.utils.Zip;
import pf.database.merchant.SubMerchantUser;
import pf.database.paymind.PmMerchantInfo;
import pf.paymind.api.JsPay;
import pf.paymind.api.RequestBean.JsPayRequestData;
import pf.paymind.api.RequestBean.ScanCodeRequestData;
import pf.paymind.api.ScanCode;

import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String scanPay() throws Exception {
        String subMerchantUserId = getParameter("id").toString();
        String body = StringUtils.convertNullableString(getParameter("body").toString());
        double total_fee = Double.parseDouble(getParameter("total_fee").toString());
        String out_trade_no = StringUtils.convertNullableString(getParameter("out_trade_no"));

        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            PmMerchantInfo pmMerchantInfo = PmMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (pmMerchantInfo == null) {
                break;
            }

            ScanCodeRequestData scanCodeRequestData = new ScanCodeRequestData();
            scanCodeRequestData.amount = total_fee;
            scanCodeRequestData.goodsName = body;
            scanCodeRequestData.merchantNo = pmMerchantInfo.getMchId();
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "paymind/"
                    + CallbackAction.JSPAYCALLBACK;
            scanCodeRequestData.serverCallbackUrl = requestUrl;
            if (!out_trade_no.isEmpty()) {
                scanCodeRequestData.orderNum = out_trade_no;
            }
            ScanCode scanCode = new ScanCode(scanCodeRequestData);
            if (scanCode.postRequest(pmMerchantInfo.getApiKey())) {
                String data = String.format("{'id':'%s','body':'%s','url':'%s','data':'%s'}",
                        subMerchantUserId,
                        body,
                        StringUtils.convertNullableString(getParameter("redirect_uri")),
                        StringUtils.convertNullableString(getParameter("data")));
                String zipData = Zip.zip(data);
                getRequest().getSession().setAttribute("data", zipData);
                SessionCache.setSessionData(scanCodeRequestData.orderNum, zipData);
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("code_url", scanCode.getPayUrl());
                return AjaxActionComplete(resultMap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public void jsPay() throws Exception {
        String subMerchantUserId = getParameter("id").toString();
        String body = StringUtils.convertNullableString(getParameter("body").toString());
        double total_fee = Double.parseDouble(getParameter("total_fee").toString());
        String out_trade_no = StringUtils.convertNullableString(getParameter("out_trade_no"));

        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            PmMerchantInfo pmMerchantInfo = PmMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (pmMerchantInfo == null) {
                break;
            }

            JsPayRequestData jsPayRequestData = new JsPayRequestData();
            jsPayRequestData.amount = total_fee;
            jsPayRequestData.goodsName = body;
            jsPayRequestData.merchantNo = pmMerchantInfo.getMchId();
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "paymind/"
                    + CallbackAction.JSPAYCALLBACK;
            jsPayRequestData.serverCallbackUrl = requestUrl;
            if (!out_trade_no.isEmpty()) {
                jsPayRequestData.orderNum = out_trade_no;
            }
            JsPay jsPay = new JsPay(jsPayRequestData);
            if (jsPay.postRequest(pmMerchantInfo.getApiKey())) {
                String data = String.format("{'id':'%s','body':'%s','url':'%s','data':'%s'}",
                        subMerchantUserId,
                        body,
                        StringUtils.convertNullableString(getParameter("redirect_uri")),
                        StringUtils.convertNullableString(getParameter("data")));
                String zipData = Zip.zip(data);
                getRequest().getSession().setAttribute("data", zipData);
                SessionCache.setSessionData(jsPayRequestData.orderNum, zipData);
                getResponse().sendRedirect(jsPay.getPayUrl());
            }
        } while (false);
    }
}
