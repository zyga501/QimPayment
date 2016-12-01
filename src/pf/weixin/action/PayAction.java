package pf.weixin.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.SessionCache;
import QimCommon.utils.StringUtils;
import QimCommon.utils.Zip;
import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.database.merchant.SubMerchantUser;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxSubMerchantInfo;
import pf.message.WeixinMessage;
import pf.weixin.api.MicroPay;
import pf.weixin.api.OpenId;
import pf.weixin.api.Refund;
import pf.weixin.api.RequestBean.MicroPayRequestData;
import pf.weixin.api.RequestBean.RefundRequestData;
import pf.weixin.api.RequestBean.UnifiedOrderRequestData;
import pf.weixin.api.UnifiedOrder;
import pf.weixin.utils.Signature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

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
                ProjectLogger.warn("MicroPay Failed!");
                return AjaxActionComplete();
            }

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("body", microPayRequestData.body);
            resultMap.put("transaction_id", StringUtils.convertNullableString(microPay.getResponseResult().get("transaction_id")));
            resultMap.put("out_trade_no", microPay.getResponseResult().get("out_trade_no").toString());
            resultMap.put("bank_type", microPay.getResponseResult().get("bank_type").toString());
            resultMap.put("total_fee", microPay.getResponseResult().get("total_fee").toString());
            resultMap.put("time_end", microPay.getResponseResult().get("time_end").toString());
            WeixinMessage.sendTemplateMessage(microPay.getResponseResult().get("transaction_id").toString());
            return AjaxActionComplete(resultMap);
        } while (false);

        return AjaxActionComplete(false);
    }

    public String scanPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

            UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
            unifiedOrderRequestData.appid = merchantInfo.getAppid();
            unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
            unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
            unifiedOrderRequestData.body = getParameter("body").toString();
            unifiedOrderRequestData.attach = String.format("{'id':'%s','body':'%s','redirect_uri':'%s','data':'%s'}",
                    StringUtils.convertNullableString(getParameter("id")),
                    unifiedOrderRequestData.body,
                    StringUtils.convertNullableString(getParameter("redirect_uri")),
                    StringUtils.convertNullableString(getParameter("data")));
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
                ProjectLogger.warn("ScanPay Failed!");
                return AjaxActionComplete();
            }

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("code_url", unifiedOrder.getResponseResult().get("code_url").toString());
            return AjaxActionComplete(resultMap);
        } while (false);

        return AjaxActionComplete(false);
    }

    public void jsPay() throws IOException {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

            String appid = merchantInfo.getAppid();
            String subMerchantUserId = getParameter("id").toString();
            String redirect_uri = getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/weixin/jsPayCallback.jsp";
            String sessionId = getRequest().getSession().getId();
            String jspayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                            "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                    appid, redirect_uri, sessionId);
            // save session data, state is too short
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
        } while (false);
    }

    public String brandWCPay() throws Exception {
        // get session data and remove data
        JSONObject jsonObject = null;
        String sessionId = StringUtils.convertNullableString(getParameter("state"));
        if (jsonObject == null && !sessionId.isEmpty()) {
            String sesseionData = SessionCache.getSessionData(sessionId).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
            }
        }

        getRequest().getSession().removeAttribute("data");
        if (!sessionId.isEmpty())
            SessionCache.clearSessionData(sessionId);

        if (jsonObject == null) {
            ProjectLogger.warn("BrandWCPay Failed! Session Data Is Miss!");
            return AjaxActionComplete(false);
        }

        String subMerchantUserId = jsonObject.get("id").toString();
        String body = jsonObject.get("body").toString();
        int total_fee = (int)Double.parseDouble(jsonObject.get("fee").toString());
        String out_trade_no = jsonObject.get("no").toString();
        String redirect_uri = jsonObject.get("url").toString();
        String data = jsonObject.get("data").toString();
        String code = getParameter("code").toString();

        if (subMerchantUserId.isEmpty() || code.isEmpty()) {
            return AjaxActionComplete();
        }

        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

            UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
            unifiedOrderRequestData.appid = merchantInfo.getAppid();
            unifiedOrderRequestData.mch_id = merchantInfo.getMchId();
            unifiedOrderRequestData.sub_mch_id = subMerchantInfo.getSubId();
            unifiedOrderRequestData.body = body;
            unifiedOrderRequestData.attach = String.format("{'id':'%s','body':'%s','redirect_uri':'%s','data':'%s'}",
                    subMerchantUserId, unifiedOrderRequestData.body, redirect_uri, data);
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
                ProjectLogger.warn("BrandWCPay Failed!");
                return AjaxActionComplete();
            }

            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("appId", unifiedOrderRequestData.appid);
            resultMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            resultMap.put("nonceStr", StringUtils.generateRandomString(32));
            resultMap.put("package", "prepay_id=" + unifiedOrder.getResponseResult().get("prepay_id").toString());
            resultMap.put("signType", "MD5");
            resultMap.put("paySign", Signature.generateSign(resultMap, merchantInfo.getApiKey()));
            resultMap.put("redirect_uri", redirect_uri);
            resultMap.put("data", data);
            return AjaxActionComplete(resultMap);
        } while (false);

        return AjaxActionComplete(false);
    }

    public String refund() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

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
                ProjectLogger.warn("Refund Failed!");
                return AjaxActionComplete();
            }
            return AjaxActionComplete(refund.getResponseResult());
        } while (false);

        return AjaxActionComplete(false);
    }
}