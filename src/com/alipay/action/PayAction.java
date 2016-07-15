package com.alipay.action;

import com.alipay.api.RequestData.TradePayRequestData;
import com.alipay.api.RequestData.TradePreCreateRequestData;
import com.alipay.api.TradePay;
import com.alipay.api.TradePreCreate;
import com.database.alipay.MerchantInfo;
import com.database.alipay.OrderInfo;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PayAction extends AjaxActionSupport {
    public String tradePay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
                if (merchantInfo != null) {
                    TradePayRequestData tradePayRequestData = new TradePayRequestData();
                    tradePayRequestData.app_id = merchantInfo.getAppid();
                    tradePayRequestData.mchId = merchantInfo.getId();
                    tradePayRequestData.scene = "bar_code";
                    tradePayRequestData.auth_code = getParameter("auth_code").toString();
                    tradePayRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
                    tradePayRequestData.subject = getParameter("subject").toString();
                    TradePay tradePay = new TradePay(tradePayRequestData, subMerchantUser.getId());
                    tradePay.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey());
                    Map<String, String> map = new HashMap<>();
                    map.put("body",getParameter("subject").toString());
                    map.put("transaction_id",tradePay.responseData.get("trade_no").toString());
                    map.put("out_trade_no", tradePay.responseData.get("out_trade_no").toString());
                    map.put("total_fee", tradePay.responseData.get("total_amount").toString());
                    map.put("time_end", tradePay.responseData.get("gmt_payment").toString());
                    return AjaxActionComplete(map);
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
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
                if (merchantInfo != null) {
                    TradePreCreateRequestData tradePreCreateRequestData = new TradePreCreateRequestData();
                    tradePreCreateRequestData.app_id = merchantInfo.getAppid();
                    tradePreCreateRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
                    tradePreCreateRequestData.subject = getParameter("subject").toString();
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "alipay/"
                            + CallbackAction.TRADEPRECREATE + "?mchId=" + merchantInfo.getId() + "&createUser=" + subMerchantUser.getId();
                    tradePreCreateRequestData.notify_url = requestUrl;
                    TradePreCreate tradePreCreate = new TradePreCreate(tradePreCreateRequestData);
                    if (tradePreCreate.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey())) {
                        if (StringUtils.convertNullableString(getParameter("auto_redirect")).compareTo("true") != 0) {
                            Map<String, String> map = new HashMap<>();
                            map.put("qr_code", tradePreCreate.getQrCode());
                            return AjaxActionComplete(map);
                        }
                        else {
                            getResponse().sendRedirect(tradePreCreate.getQrCode());
                        }
                    }
                }
            }
        }

        return AjaxActionComplete(false);
    }

    public String queryOrder() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
//                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
//                if (merchantInfo != null) {
                    OrderInfo orderInfo = OrderInfo.getOrderInfoByOrderNo(getParameter("out_trade_no").toString());
                    List<OrderInfo> list = new ArrayList<>();
                    list.add(orderInfo);
                    return AjaxActionComplete(list);
//                }
            }
        }
        return AjaxActionComplete();
    }
}
