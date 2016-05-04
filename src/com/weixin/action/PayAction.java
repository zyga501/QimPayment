package com.weixin.action;

import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.database.weixin.SubMerchantInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;
import com.framework.utils.UdpSocket;
import com.framework.utils.Zip;
import com.message.WeixinMessage;
import com.weixin.api.MicroPay;
import com.weixin.api.OpenId;
import com.weixin.api.Refund;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.RefundRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.api.UnifiedOrder;
import com.weixin.utils.Signature;
import net.sf.json.JSONObject;

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
                    microPayRequestData.attach = "{ 'id':'" + subMerchantUser.getId() + "', 'body':'" + microPayRequestData.body + "'}";
                    microPayRequestData.total_fee = (int)Double.parseDouble(getParameter("total_fee").toString());
                    microPayRequestData.auth_code = getParameter("auth_code").toString();
                    if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                        microPayRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    if (!StringUtils.convertNullableString(getParameter("goods_tag")).isEmpty()) {
                        microPayRequestData.goods_tag = getParameter("goods_tag").toString();
                    }
                    MicroPay microPay = new MicroPay(microPayRequestData, subMerchantUser.getId());
                    if (!microPay.postRequest(merchantInfo.getApiKey())) {
                        Logger.warn("MicroPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("body", microPayRequestData.body);
                    map.put("transaction_id", microPay.getResponseResult().get("transaction_id").toString());
                    map.put("out_trade_no", microPay.getResponseResult().get("out_trade_no").toString());
                    map.put("bank_type", microPay.getResponseResult().get("bank_type").toString());
                    map.put("total_fee", microPay.getResponseResult().get("total_fee").toString());
                    map.put("time_end", microPay.getResponseResult().get("time_end").toString());

                    new UdpSocket("127.0.0.1", 8848).send(String.valueOf(subMerchantUser.getId()).concat("@").concat(JSONObject.fromObject(map).toString()).getBytes()); // notify client to print
                    WeixinMessage.sendTemplateMessage(microPay.getResponseResult().get("transaction_id").toString());

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
                    unifiedOrderRequestData.attach = String.format("{ 'id':'%s','body':'%s','redirect_uri':'%s'}",
                            StringUtils.convertNullableString(getParameter("id")), unifiedOrderRequestData.body, StringUtils.convertNullableString(getParameter("redirect_uri")));
                    unifiedOrderRequestData.total_fee = (int)Double.parseDouble(getParameter("total_fee").toString());
                    unifiedOrderRequestData.product_id = getParameter("product_id").toString();
                    unifiedOrderRequestData.trade_type = "NATIVE";
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/"
                            + CallbackAction.SCANPAYCALLBACK;;
                    unifiedOrderRequestData.notify_url = requestUrl;
                    if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                        unifiedOrderRequestData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);

                    if (!unifiedOrder.postRequest(merchantInfo.getApiKey())) {
                        Logger.warn("ScanPay Failed!");
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

    public void jsPay() throws IOException {
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

        if (appid.isEmpty()) {
            Logger.warn("JsPay Failed!");
            return;
        }

        String redirect_uri = getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/weixin/jsPayCallback.jsp";
        String data = String.format("{'id':'%s','body':'%s','fee':'%s','no':'%s','url':'%s'}",
                subMerchantUserId,
                new String(StringUtils.convertNullableString(getParameter("body")).getBytes("iso-8859-1"), "utf-8"),
                StringUtils.convertNullableString(getParameter("total_fee")),
                StringUtils.convertNullableString(getParameter("out_trade_no")),
                StringUtils.convertNullableString(getParameter("redirect_uri")));
        String jspayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                        "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect",
                appid, redirect_uri);
        // save session data, state is too short
        getRequest().getSession().setAttribute("data", Zip.zip(data));
        getResponse().sendRedirect(jspayUri);
    }

    public String brandWCPay() throws Exception {
        // get session data and remove data
        JSONObject jsonObject = JSONObject.fromObject(Zip.unZip(getParameter("data").toString()));
        getRequest().getSession().removeAttribute("data");
        String subMerchantUserId = jsonObject.get("id").toString();
        String body = jsonObject.get("body").toString();
        int total_fee = (int)Double.parseDouble(jsonObject.get("fee").toString());
        String out_trade_no = jsonObject.get("no").toString();
        String redirect_uri = jsonObject.get("url").toString();
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
                    unifiedOrderRequestData.body = body;
                    unifiedOrderRequestData.attach = String.format("{ 'id':'%s','body':'%s','redirect_uri':'%s'}",
                            subMerchantUserId, unifiedOrderRequestData.body, redirect_uri);
                    Logger.info("unifiedOrderRequestData.attach:" + unifiedOrderRequestData.attach);
                    unifiedOrderRequestData.total_fee = total_fee;
                    unifiedOrderRequestData.trade_type = "JSAPI";
                    OpenId openId = new OpenId(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                    if (openId.getRequest()) {
                        unifiedOrderRequestData.openid = openId.getOpenId();
                    }
                    else {
                        return AjaxActionComplete();
                    }
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/"
                            + CallbackAction.BRANDWCPAYCALLBACK;
                    unifiedOrderRequestData.notify_url = requestUrl;
                    if (!out_trade_no.isEmpty()) {
                        unifiedOrderRequestData.out_trade_no = out_trade_no;
                    }

                    UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
                    if (!unifiedOrder.postRequest(merchantInfo.getApiKey())) {
                        Logger.warn("BrandWCPay Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String,Object> map = new HashMap<>();
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
                        Logger.warn("Refund Failed!");
                        return AjaxActionComplete();
                    }
                    return AjaxActionComplete(refund.getResponseResult());
                }
            }
        }

        return AjaxActionComplete();
    }
}