package pf.swiftpass.action;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import framework.action.AjaxActionSupport;
import framework.utils.StringUtils;
import pf.ProjectLogger;
import pf.database.merchant.SubMerchantUser;
import pf.database.swiftpass.SwiftMerchantInfo;
import pf.swiftpass.api.AliJsPay;
import pf.swiftpass.api.RequestBean.AliJsPayRequestData;
import pf.swiftpass.api.RequestBean.WeixinJsPayRequestData;
import pf.swiftpass.api.WeixinJsPay;
import pf.weixin.api.OpenId;

import java.io.IOException;

public class PayAction extends AjaxActionSupport {
    public String weixinJsPay() throws Exception {
        if (getParameter("code") == null || getParameter("state") == null) {
            return "fetchWxCode";
        }

        do {
            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(Long.parseLong(getParameter("state").toString()));
            if (swiftMerchantInfo == null) {
                break;
            }

            WeixinJsPayRequestData weixinJsPayRequestData = new WeixinJsPayRequestData();
            weixinJsPayRequestData.mch_id = swiftMerchantInfo.getMchId();
            weixinJsPayRequestData.body = "企盟支付";
            weixinJsPayRequestData.attach = getParameter("state").toString();
            weixinJsPayRequestData.total_fee = (int)Double.parseDouble(getParameter("total_amount").toString());
                OpenId weixinOpenId = new OpenId(swiftMerchantInfo.getWeixinAppId(), swiftMerchantInfo.getWeixinAppSecret(), getParameter("code").toString());
            if (!weixinOpenId.getRequest()) {
                return AjaxActionComplete(false);
            }
            weixinJsPayRequestData.sub_openid = weixinOpenId.getOpenId();
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "swiftpass/"
                    + CallbackAction.WEIXINJSPAYCALLBACK;
            weixinJsPayRequestData.notify_url = requestUrl;
            if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                weixinJsPayRequestData.out_trade_no = getParameter("out_trade_no").toString();
            }

            WeixinJsPay jsPay = new WeixinJsPay(weixinJsPayRequestData);
            if (jsPay.postRequest(swiftMerchantInfo.getApiKey())) {
                getResponse().sendRedirect(jsPay.getRedirectUrl());
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public void fetchWxCode() throws IOException {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                break;
            }

            String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/swiftpass/weixinjspay.jsp";
            String fetchOpenidUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                            "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%d#wechat_redirect",
                    swiftMerchantInfo.getWeixinAppId(), redirect_uri, subMerchantUser.getSubMerchantId());
            getResponse().sendRedirect(fetchOpenidUri);
        } while (false);
    }

    public void aliJsPay() throws Exception {
        if (getParameter("auth_code") == null) {
            fetchBuyerId();
            return;
        }

        do {
            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(Long.parseLong(getParameter("state").toString()));
            if (swiftMerchantInfo == null) {
                break;
            }

            AliJsPayRequestData aliJsPayRequestData = new AliJsPayRequestData();
            aliJsPayRequestData.mch_id = swiftMerchantInfo.getMchId();
            aliJsPayRequestData.body = "企盟支付";
            aliJsPayRequestData.attach = getParameter("state").toString();
            aliJsPayRequestData.total_fee = (int)Double.parseDouble(getParameter("total_amount").toString());
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "swiftpass/"
                    + CallbackAction.ALIJSPAYCALLBACK;
            aliJsPayRequestData.notify_url = requestUrl;
            aliJsPayRequestData.buyer_id = fetchBuyerId(getParameter("auth_code").toString(), swiftMerchantInfo.getAliAppId(), swiftMerchantInfo.getAliPrivateKey(), swiftMerchantInfo.getAliPublicKey());
            if (!StringUtils.convertNullableString(getParameter("out_trade_no")).isEmpty()) {
                aliJsPayRequestData.out_trade_no = getParameter("out_trade_no").toString();
            }

            AliJsPay aliJsPay = new AliJsPay(aliJsPayRequestData);
            if (aliJsPay.postRequest(swiftMerchantInfo.getApiKey())) {
                getResponse().sendRedirect(aliJsPay.getPayUrl());
            }
        } while (false);
    }

    void fetchBuyerId() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            SwiftMerchantInfo swiftMerchantInfo = SwiftMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (swiftMerchantInfo == null) {
                ProjectLogger.error("Fetch SwitfPass BuyerId Failed!");
                break;
            }

            String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/swiftpass/alijspay.jsp";
            String fetchOpenidUri = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_base&redirect_uri=%s&state=%d",
                    swiftMerchantInfo.getAliAppId(), redirect_uri, subMerchantUser.getSubMerchantId());
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
}
