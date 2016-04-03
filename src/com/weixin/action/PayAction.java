package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.weixin.api.MicroPay;
import com.weixin.api.OrderQuery;
import com.weixin.api.Refund;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.OrderQueryData;
import com.weixin.api.RequestData.RefundRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.api.UnifiedOrder;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;
import com.weixin.database.SubMerchantUser;
import com.weixin.utils.OAuth2;
import com.weixin.utils.Signature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    MicroPayRequestData microPayRequestData = new MicroPayRequestData();
                    microPayRequestData.appid = merchantInfo.getAppid();
                    microPayRequestData.mch_id = merchantInfo.getMchId();
                    microPayRequestData.sub_mch_id = subMerchantInfo.getSubId();
                    microPayRequestData.body = getParameter("body").toString();
                    microPayRequestData.attach = microPayRequestData.body;
                    microPayRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    microPayRequestData.auth_code = getParameter("auth_code").toString();
                    MicroPay microPay = new MicroPay(microPayRequestData, subMerchantUser.getId());
                    if (!microPay.execute(merchantInfo.getApiKey())) {
                        System.out.println("MicroPay Failed!");
                        return AjaxActionComplete();
                    }
                    return AjaxActionComplete(microPay.getResponseResult());
                }
            }
        }

        return AjaxActionComplete();
    }

    public String scanPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
                    unifiedOrderRequestData.appid = merchantInfo.getAppid();
                    unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
                    unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
                    unifiedOrderRequestData.body = getParameter("body").toString();
                    unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    unifiedOrderRequestData.product_id = getParameter("product_id").toString();
                    unifiedOrderRequestData.trade_type = "NATIVE";
                    unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.SCANPAYCALLBACK;
                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);

                    if (!unifiedOrder.execute(merchantInfo.getApiKey())) {
                        System.out.println("ScanPay Failed!");
                        return AjaxActionComplete();
                    }
                    return AjaxActionComplete(unifiedOrder.getResponseResult());
                }
            }
        }
        return AjaxActionComplete();
    }

    public void prePay() throws IOException {
        String appid = new String();
        String subMerchantUserId = new String();
        if (getParameter("id") != null) {
            subMerchantUserId = getParameter("id").toString();
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
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
        else { // compatible old api
            subMerchantUserId = getParameter("odod").toString();
            // TODO
        }

        if (appid.isEmpty()) {
            System.out.println("PrePay Failed!");
            return;
        }

        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "index.jsp";
        String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                appid, redirect_uri, subMerchantUserId);
        getResponse().sendRedirect(perPayUri);
    }

    public String brandWCPay() throws Exception {
        String subMerchantUserId = getParameter("state").toString();
        String code = getParameter("code").toString();
        if (subMerchantUserId.isEmpty() || code.isEmpty()) {
            return AjaxActionComplete();
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
                    unifiedOrderRequestData.appid = merchantInfo.getAppid();
                    unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
                    unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
                    unifiedOrderRequestData.body = getParameter("body").toString();
                    unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    unifiedOrderRequestData.trade_type = "JSAPI";
                    unifiedOrderRequestData.openid = OAuth2.fetchOpenid(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                    unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.BRANDWCPAYCALLBACK;

                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
                    if (!unifiedOrder.execute(merchantInfo.getApiKey())) {
                        System.out.println("BrandWCPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("appId", unifiedOrderRequestData.appid);
                    map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                    map.put("nonceStr", StringUtils.generateRandomString(32));
                    map.put("package", "prepay_id=" + unifiedOrder.getResponseResult().get("prepay_id").toString());
                    map.put("signType", "MD5");
                    map.put("paySign", Signature.generateSign(map, merchantInfo.getApiKey()));
                    return AjaxActionComplete(map);
                }
            }
        }

        return AjaxActionComplete();
    }

    public String refund() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    RefundRequestData refundRequestData = new RefundRequestData();
                    refundRequestData.appid = merchantInfo.getAppid();
                    refundRequestData.mch_id = merchantInfo.getMchId();
                    refundRequestData.sub_mch_id = subMerchantInfo.getSubId();
                    refundRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    refundRequestData.refund_fee = Integer.parseInt(getParameter("refund_fee").toString());
                    refundRequestData.transaction_id = getParameter("transaction_id").toString();
                    refundRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    refundRequestData.op_user_id = refundRequestData.mch_id;
                    Refund refund = new Refund(refundRequestData);
                    if (!refund.execute(merchantInfo.getApiKey())) {
                        System.out.println("Refund Failed!");
                        return AjaxActionComplete();
                    }
                    return AjaxActionComplete(refund.getResponseResult());
                }
            }
        }

        return AjaxActionComplete();
    }

    public String orderQuery() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    OrderQueryData orderQueryData = new OrderQueryData();
                    orderQueryData.appid = merchantInfo.getAppid();
                    orderQueryData.mch_id = merchantInfo.getMchId();
                    orderQueryData.sub_mch_id = subMerchantInfo.getSubId();
                    orderQueryData.transaction_id = getParameter("transaction_id").toString();
                    orderQueryData.out_trade_no = getParameter("out_trade_no").toString();
                    OrderQuery orderQuery = new OrderQuery(orderQueryData);
                    if (!orderQuery.execute(merchantInfo.getApiKey())) {
                        System.out.println("Refund Failed!");
                        return AjaxActionComplete();
                    }
                    return AjaxActionComplete(orderQuery.getResponseResult());
                }
            }
        }

        return AjaxActionComplete();
    }
}
