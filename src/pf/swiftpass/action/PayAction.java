package pf.swiftpass.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.SessionCache;
import QimCommon.utils.StringUtils;
import QimCommon.utils.Zip;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.database.merchant.SubMerchantUser;
import pf.database.swiftpass.SwiftMerchantInfo;
import pf.swiftpass.api.AliJsPay;
import pf.swiftpass.api.RequestBean.AliJsPayRequestData;
import pf.swiftpass.api.RequestBean.WeixinJsPayRequestData;
import pf.swiftpass.api.RequestBean.WeixinNativeRequestData;
import pf.swiftpass.api.WeixinJsPay;
import pf.swiftpass.api.WeixinNative;
import pf.weixin.api.OpenId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String weixinJsPay() throws Exception {
        if (getParameter("code") == null || getParameter("state") == null) {
            return "fetchWxCode";
        }

        // get session data and remove data
        JSONObject jsonObject = null;
        String sessionId = StringUtils.convertNullableString(getParameter("state"));
        if (!sessionId.isEmpty()) {
            String sesseionData = SessionCache.getSessionData(sessionId).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
            }
        }

        getRequest().getSession().removeAttribute("data");
        if (!sessionId.isEmpty())
            SessionCache.clearSessionData(sessionId);

        if (jsonObject == null) {
            ProjectLogger.warn("weixinJsPay Failed! Session Data Is Miss!");
            return AjaxActionComplete(false);
        }

        String subMerchantUserId = jsonObject.get("id").toString();
        String body = jsonObject.get("body").toString();
        int total_fee = (int)Double.parseDouble(jsonObject.get("fee").toString());
        String out_trade_no = jsonObject.get("no").toString();
        String redirect_uri = jsonObject.get("url").toString();
        String data = jsonObject.get("data").toString();
        String code = getParameter("code").toString();

        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                break;
            }

            WeixinJsPayRequestData weixinJsPayRequestData = new WeixinJsPayRequestData();
            weixinJsPayRequestData.mch_id = swiftMerchantInfo.getMchId();
            weixinJsPayRequestData.body = body;
            weixinJsPayRequestData.total_fee = total_fee;
            if (!out_trade_no.isEmpty()) {
                weixinJsPayRequestData.out_trade_no = out_trade_no;
            }
            SessionCache.setSessionData(weixinJsPayRequestData.out_trade_no, Zip.zip(String.format("{'id':'%s','body':'%s','url':'%s','data':'%s'}",
                    subMerchantUserId, body, redirect_uri, data)));
            weixinJsPayRequestData.attach = weixinJsPayRequestData.out_trade_no;

            OpenId weixinOpenId = new OpenId(swiftMerchantInfo.getWeixinAppId(), swiftMerchantInfo.getWeixinAppSecret(), code);
            if (!weixinOpenId.getRequest()) {
                return AjaxActionComplete(false);
            }
            weixinJsPayRequestData.sub_openid = weixinOpenId.getOpenId();
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "swiftpass/"
                    + CallbackAction.WEIXINJSPAYCALLBACK;
            weixinJsPayRequestData.notify_url = requestUrl;

            WeixinJsPay jsPay = new WeixinJsPay(weixinJsPayRequestData);
            if (jsPay.postRequest(swiftMerchantInfo.getApiKey())) {
                getResponse().sendRedirect(jsPay.getRedirectUrl());
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public void fetchWxCode() throws IOException {
        do {
            String subMerchantUserId = getParameter("id").toString();
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                break;
            }

            String sessionId = getRequest().getSession().getId();
            String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/swiftpass/Pay!weixinJsPay";
            String fetchOpenidUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                            "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                    swiftMerchantInfo.getWeixinAppId(), redirect_uri, sessionId);
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
            getResponse().sendRedirect(fetchOpenidUri);
        } while (false);
    }

    public void aliJsPay() throws Exception {
        if (getParameter("auth_code") == null || getParameter("state") == null) {
            fetchBuyerId();
            return;
        }

        do {
            // get session data and remove data
            JSONObject jsonObject = null;
            String sessionId = StringUtils.convertNullableString(getParameter("state"));
            if (!sessionId.isEmpty()) {
                String sesseionData = SessionCache.getSessionData(sessionId).toString();
                if (!sesseionData.isEmpty()) {
                    jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
                }
            }

            getRequest().getSession().removeAttribute("data");
            if (!sessionId.isEmpty())
                SessionCache.clearSessionData(sessionId);

            if (jsonObject == null) {
                ProjectLogger.warn("aliJsPay Failed! Session Data Is Miss!");
                return;
            }

            String subMerchantUserId = jsonObject.get("id").toString();
            String body = jsonObject.get("body").toString();
            int total_fee = (int)Double.parseDouble(jsonObject.get("fee").toString());
            String out_trade_no = jsonObject.get("no").toString();
            String redirect_uri = jsonObject.get("url").toString();
            String data = jsonObject.get("data").toString();

            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                break;
            }

            AliJsPayRequestData aliJsPayRequestData = new AliJsPayRequestData();
            aliJsPayRequestData.mch_id = swiftMerchantInfo.getMchId();
            aliJsPayRequestData.body = body;
            aliJsPayRequestData.total_fee = total_fee;
            if (!out_trade_no.isEmpty()) {
                aliJsPayRequestData.out_trade_no = out_trade_no;
            }
            SessionCache.setSessionData(aliJsPayRequestData.out_trade_no, Zip.zip(String.format("{'id':'%s','body':'%s','url':'%s','data':'%s'}",
                    subMerchantUserId, body, redirect_uri, data)));
            aliJsPayRequestData.attach = aliJsPayRequestData.out_trade_no;

            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "swiftpass/"
                    + CallbackAction.ALIJSPAYCALLBACK;
            aliJsPayRequestData.notify_url = requestUrl;
            aliJsPayRequestData.buyer_id = fetchBuyerId(getParameter("auth_code").toString(), swiftMerchantInfo.getAliAppId(), swiftMerchantInfo.getAliPrivateKey(), swiftMerchantInfo.getAliPublicKey());

            AliJsPay aliJsPay = new AliJsPay(aliJsPayRequestData);
            if (aliJsPay.postRequest(swiftMerchantInfo.getApiKey())) {
                getResponse().sendRedirect(aliJsPay.getPayUrl());
            }
        } while (false);
    }

    void fetchBuyerId() throws Exception {
        do {
            String subMerchantUserId = getParameter("id").toString();
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            String sessionId = getRequest().getSession().getId();
            String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/swiftpass/Pay!aliJsPay";
            String fetchOpenidUri = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_base&redirect_uri=%s&state=%s",
                    swiftMerchantInfo.getAliAppId(), redirect_uri, sessionId);
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
            getResponse().sendRedirect(fetchOpenidUri);
        } while (false);
    }

    String fetchBuyerId(String auth_code, String appid, String privateKey, String publicKey) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appid, privateKey, "json", "GBK", publicKey);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(auth_code);
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getUserId();
        }

        ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
        return "";
    }

    public String weixinNative() throws Exception {
        do {
            String subMerchantUserId = getParameter("id").toString();
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            String body = getParameter("body").toString();
            int total_fee = (int)Double.parseDouble(getParameter("total_fee").toString());

            WeixinNativeRequestData weixinNativeRequestData = new WeixinNativeRequestData();
            weixinNativeRequestData.mch_id = swiftMerchantInfo.getMchId();
            weixinNativeRequestData.body = body;
            weixinNativeRequestData.total_fee = total_fee;
            if (getParameter("out_trade_no") != null) {
                weixinNativeRequestData.out_trade_no = getParameter("out_trade_no").toString();
            }
            weixinNativeRequestData.attach = weixinNativeRequestData.out_trade_no;
            SessionCache.setSessionData(weixinNativeRequestData.out_trade_no, Zip.zip(String.format("{'id':'%s','body':'%s','url':'%s','data':'%s'}",
                    subMerchantUserId,
                    body,
                    StringUtils.convertNullableString(getParameter("redirect_uri")),
                    StringUtils.convertNullableString(getParameter("data")))));
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "swiftpass/"
                    + CallbackAction.WEIXINNATIVECALLBACK;
            weixinNativeRequestData.notify_url = requestUrl;
            WeixinNative weixinNative = new WeixinNative(weixinNativeRequestData);
            if (weixinNative.postRequest(swiftMerchantInfo.getApiKey())) {
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("code_url", weixinNative.getCodeUrl());
                return AjaxActionComplete(resultMap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }
}
