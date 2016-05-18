package com.alipay.action;

import com.alipay.api.RequestData.TradePayRequestData;
import com.alipay.api.RequestData.TradePreCreateRequestData;
import com.alipay.api.TradePay;
import com.alipay.api.TradePreCreate;
import com.database.alipay.MerchantInfo;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;

import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String tradePay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    TradePayRequestData tradePayRequestData = new TradePayRequestData();
                    tradePayRequestData.app_id = merchantInfo.getAppid();
                    tradePayRequestData.scene = "bar_code";
                    tradePayRequestData.auth_code = getParameter("auth_code").toString();
                    tradePayRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
                    tradePayRequestData.subject = getParameter("subject").toString();
                    TradePay tradePay = new TradePay(tradePayRequestData);
                    return AjaxActionComplete(tradePay.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey()));
                }
            }
        }

        return AjaxActionComplete(false);
    }

    public String tradePreCreate() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    TradePreCreateRequestData tradePreCreateRequestData = new TradePreCreateRequestData();
                    tradePreCreateRequestData.app_id = merchantInfo.getAppid();
                    tradePreCreateRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
                    tradePreCreateRequestData.subject = getParameter("subject").toString();
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "alipay/"
                            + CallbackAction.TRADEPRECREATE;;
                    tradePreCreateRequestData.notify_url = requestUrl;
                    TradePreCreate tradePreCreate = new TradePreCreate(tradePreCreateRequestData);
                    if (tradePreCreate.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey())) {
                        Map<String, String> map = new HashMap<>();
                        map.put("qr_code", tradePreCreate.getQrCode());
                        return AjaxActionComplete(map);
                    }
                }
            }
        }

        return AjaxActionComplete(false);
    }
}
