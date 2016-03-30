package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.weixin.api.MicroPay;
import com.weixin.api.Refund;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.RefundRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.api.UnifiedOrder;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;
import com.weixin.utils.OAuth2;
import com.weixin.utils.Signature;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(getParameter("sub_mch_id").toString());
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                MicroPayRequestData microPayRequestData = new MicroPayRequestData();
                microPayRequestData.appid = merchantInfo.getAppid();
                microPayRequestData.mch_id = merchantInfo.getMchId();
                microPayRequestData.sub_mch_id = subMerchantInfo.getSubId();
                microPayRequestData.body = getParameter("productBody").toString();
                microPayRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
                microPayRequestData.auth_code = getParameter("auth_code").toString();
                MicroPay microPay = new MicroPay(microPayRequestData);
                if (!microPay.execute(merchantInfo.getApiKey())) {
                    System.out.println("MicroPay Failed!");
                }
            }
        }

        return AjaxActionComplete();
    }

    public String scanPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(getParameter("sub_mch_id").toString());
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
                unifiedOrderRequestData.appid = merchantInfo.getAppid();
                unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
                unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
                unifiedOrderRequestData.body = getParameter("productBody").toString();
                unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
                unifiedOrderRequestData.product_id = getParameter("auth_code").toString();
                unifiedOrderRequestData.trade_type = "NATIVE";
                unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.SCANPAYCALLBACK;
                UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);

                Map map = new HashMap();
                if (unifiedOrder.execute(merchantInfo.getApiKey())) {
                    map.put("code_url", unifiedOrder.getCodeUrl());
                    return AjaxActionComplete(map);
                }
                else {
                    System.out.println("ScanPay Failed!");
                }
            }
        }
        return AjaxActionComplete();
    }

    public void prePay() throws IOException {
        String appid = new String();
        String subMerchantId = new String();
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(getParameter("subMerchantId").toString());
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                    appid = merchantInfo.getAppid();
                    subMerchantId = subMerchantInfo.getSubId();
                }
        }
        else { // compatible old api
            String subMerchantUserId = getParameter("odod").toString();
            // TODO
        }

        if (appid.isEmpty()) {
            System.out.println("PrePay Failed!");
        }

        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "index.jsp";
        String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                appid, redirect_uri, subMerchantId);
        getResponse().sendRedirect(perPayUri);
    }

    public String brandWCPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        String subMerchantId = getParameter("state").toString();
        String code = getParameter("code").toString();
        if (subMerchantId.isEmpty() || code.isEmpty()) {
            return AjaxActionComplete();
        }

        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(subMerchantId);
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
                unifiedOrderRequestData.appid = merchantInfo.getAppid();
                unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
                unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
                unifiedOrderRequestData.body = getParameter("productBody").toString();
                unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
                unifiedOrderRequestData.trade_type = "JSAPI";
                unifiedOrderRequestData.openid = OAuth2.fetchOpenid(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.BRANDWCPAYCALLBACK;

                UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
                Map map = new HashMap();
                if (unifiedOrder.execute(merchantInfo.getApiKey())) {
                    map.put("appId", unifiedOrderRequestData.appid);
                    map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                    map.put("nonceStr", StringUtils.generateRandomString(32));
                    map.put("package", "prepay_id=" + unifiedOrder.getPrepayID());
                    map.put("signType", "MD5");
                    map.put("paySign", Signature.generateSign(map, merchantInfo.getApiKey()));
                    return AjaxActionComplete(map);
                }
            }
        }

        return AjaxActionComplete();
    }

    public String refund() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(getParameter("sub_mch_id").toString());
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                RefundRequestData refundRequestData = new RefundRequestData();
                refundRequestData.appid = merchantInfo.getAppid();
                refundRequestData.mch_id = merchantInfo.getMchId();
                refundRequestData.sub_mch_id = subMerchantInfo.getSubId();
                refundRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
                refundRequestData.refund_fee = Integer.parseInt(getParameter("refund_fee").toString());
                refundRequestData.transaction_id = getParameter("transaction_id").toString();
                refundRequestData.out_trade_no = getParameter("out_trade_no").toString();
                refundRequestData.op_user_id = refundRequestData.mch_id;
                Refund refund = new Refund(refundRequestData);
                refund.execute(merchantInfo.getApiKey());
            }
        }

        return AjaxActionComplete();
    }
}
