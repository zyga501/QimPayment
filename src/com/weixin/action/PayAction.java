package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.merchant.database.IdMapUUID;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.*;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.OrderQueryData;
import com.weixin.api.RequestData.RefundRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;
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
                    if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                        microPayRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    if (!StringUtils.convertNullableString(getParameter("goods_tag")).isEmpty()) {
                        microPayRequestData.goods_tag = getParameter("goods_tag").toString();
                    }
                    MicroPay microPay = new MicroPay(microPayRequestData, subMerchantUser.getId());
                    if (!microPay.postRequest(merchantInfo.getApiKey())) {
                        System.out.println("MicroPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("body", microPay.getResponseResult().get("attach").toString());
                    map.put("transaction_id", microPay.getResponseResult().get("transaction_id").toString());
                    map.put("out_trade_no", microPay.getResponseResult().get("out_trade_no").toString());
                    map.put("bank_type", microPay.getResponseResult().get("bank_type").toString());
                    map.put("total_fee", microPay.getResponseResult().get("total_fee").toString());
                    map.put("time_end", microPay.getResponseResult().get("time_end").toString());
                    return AjaxActionComplete(map);
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
                    unifiedOrderRequestData.attach = "{ 'id':'" + getParameter("id").toString() + "', 'body':'" + unifiedOrderRequestData.body + "'}";
                    unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    unifiedOrderRequestData.product_id = getParameter("product_id").toString();
                    unifiedOrderRequestData.trade_type = "NATIVE";
                    unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1)
                            + CallbackAction.SCANPAYCALLBACK;
                    if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                        unifiedOrderRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    if (!StringUtils.convertNullableString(getParameter("goods_tag")).isEmpty()) {
                        unifiedOrderRequestData.goods_tag = getParameter("goods_tag").toString();
                    }
                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);

                    if (!unifiedOrder.postRequest(merchantInfo.getApiKey())) {
                        System.out.println("ScanPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("code_url", unifiedOrder.getResponseResult().get("code_url").toString());
                    return AjaxActionComplete(map);
                }
            }
        }
        return AjaxActionComplete();
    }

    public void prePay() throws IOException {
        String appid = new String();
        String subMerchantUserId = new String();
        if (!StringUtils.convertNullableString(getParameter("id")).isEmpty()) {
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
           IdMapUUID idMapUUID= IdMapUUID.getMappingByUUID( getParameter("odod").toString());
           if (idMapUUID != null) {
               subMerchantUserId = String.valueOf(idMapUUID.getId());
               SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(idMapUUID.getId());
               getRequest().getSession().setAttribute("storename",subMerchantUser.getStoreName());
               getRequest().getSession().setAttribute("ucode",subMerchantUser.getUserName());
               SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
               MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
               getRequest().getSession().setAttribute("subMerchantId",subMerchantInfo.getId());
               appid = merchantInfo.getAppid();
           }
        }

        if (appid.isEmpty()) {
            System.out.println("PrePay Failed!");
            return;
        }

        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "weixin/oauthpay.jsp";
        if (!StringUtils.convertNullableString(getParameter("redirect_uri")).isEmpty()) {
            redirect_uri = getParameter("redirect_uri").toString();
        }
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
                    unifiedOrderRequestData.attach = "{ 'id':'" + subMerchantUserId + "', 'body':'" + unifiedOrderRequestData.body + "'}";
                    unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
                    unifiedOrderRequestData.trade_type = "JSAPI";
                    OpenId openId = new OpenId(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                    if (openId.getRequest()) {
                        unifiedOrderRequestData.openid = openId.getOpenId();
                    }
                    else {
                        System.out.println(this.getClass().getName() + "Get OpenId Failed!");
                        return AjaxActionComplete();
                    }
                    unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1)
                            + CallbackAction.BRANDWCPAYCALLBACK;
                    if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                        unifiedOrderRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    if (!StringUtils.convertNullableString(getParameter("goods_tag")).isEmpty()) {
                        unifiedOrderRequestData.goods_tag = getParameter("goods_tag").toString();
                    }

                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
                    if (!unifiedOrder.postRequest(merchantInfo.getApiKey())) {
                        System.out.println("BrandWCPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map map = new HashMap<>();
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
                    if (getParameter("transaction_id") != null) {
                        refundRequestData.transaction_id = getParameter("transaction_id").toString();
                    }
                    if (getParameter("out_trade_no") != null) {
                        refundRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    refundRequestData.op_user_id = refundRequestData.mch_id;
                    Refund refund = new Refund(refundRequestData);
                    if (!refund.postRequest(merchantInfo.getApiKey())) {
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
                    if (getParameter("transaction_id") != null) {
                        orderQueryData.transaction_id = getParameter("transaction_id").toString();
                    }
                    if (getParameter("out_trade_no") != null) {
                        orderQueryData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    OrderQuery orderQuery = new OrderQuery(orderQueryData);
                    if (!orderQuery.postRequest(merchantInfo.getApiKey())) {
                        System.out.println("Refund Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    if (null !=orderQuery.getResponseResult().get("attach")) {
                        map.put("body", orderQuery.getResponseResult().get("attach").toString());
                    }
                    else {
                        map.put("body", "");
                    }
                    map.put("transaction_id", orderQuery.getResponseResult().get("transaction_id").toString());
                    map.put("out_trade_no", orderQuery.getResponseResult().get("out_trade_no").toString());
                    map.put("bank_type", orderQuery.getResponseResult().get("bank_type").toString());
                    map.put("total_fee", orderQuery.getResponseResult().get("total_fee").toString());
                    map.put("time_end", orderQuery.getResponseResult().get("time_end").toString());
                    return AjaxActionComplete(map);
                }
            }
        }

        return AjaxActionComplete();
    }
}