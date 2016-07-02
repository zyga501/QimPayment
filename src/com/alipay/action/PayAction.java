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
import com.framework.base.SessionCache;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;
import com.framework.utils.Zip;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
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

    public void jsPay() throws IOException {
        String appid = new String();
        String subMerchantUserId = new String();
        if (!StringUtils.convertNullableString(getParameter("id")).isEmpty()) {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            subMerchantUserId = getParameter("id").toString();
            if (subMerchantUser != null) {
                SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
                if (subMerchantInfo != null) {
                    MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        appid = merchantInfo.getAppid();
                    }
                }
            }
        }

        if (appid.isEmpty()) {
            Logger.warn("JsPay Failed!");
            return;
        }

        String sessionId = getRequest().getSession().getId();
        String redirect_uri = getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/alipay/jsPayCallback.jsp";
        redirect_uri += "?state=" + sessionId;
        String jspayUri = String.format("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=%s&redirect_uri=%s",
                appid, redirect_uri);

        String data = String.format("{'id':'%s','body':'%s','fee':'%s','no':'%s','url':'%s','data':'%s'}",
                subMerchantUserId,
                StringUtils.convertNullableString(getParameter("body")),
                StringUtils.convertNullableString(getParameter("total_fee")),
                StringUtils.convertNullableString(getParameter("out_trade_no")),
                StringUtils.convertNullableString(getParameter("redirect_uri")),
                StringUtils.convertNullableString(getParameter("data")));
        String zipData = Zip.zip(data);
        getRequest().getSession().setAttribute("data", zipData);
        SessionCache.setSessionData(sessionId, zipData);
        getResponse().sendRedirect(jspayUri);
    }

    public String wapPay() throws IOException {
        return AjaxActionComplete();
    }

    public String queryOrder() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo = OrderInfo.getOrderInfoByOrderNo(getParameter("out_trade_no").toString());
                    List list = null;
                    list.add(orderInfo);
                    return AjaxActionComplete(list);
                }
            }
        }
        return AjaxActionComplete();
    }
}
