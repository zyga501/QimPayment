package com.alipay.action;

import com.alipay.api.RequestData.TradePayRequestData;
import com.alipay.api.TradePay;
import com.database.alipay.MerchantInfo;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;

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
                    tradePay.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey());
                }
            }
        }

        return AjaxActionComplete();
    }
}
