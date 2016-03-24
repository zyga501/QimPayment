package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.weixin.api.MicroPay;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.api.UnifiedOrder;
import com.weixin.utils.OAuth2;
import com.weixin.utils.Signature;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        MicroPayRequestData microPayRequestData = new MicroPayRequestData();
        microPayRequestData.appid = getParameter("appid").toString();
        microPayRequestData.mch_id = getParameter("mch_id").toString();
        microPayRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
        microPayRequestData.body = getParameter("productBody").toString();
        microPayRequestData.out_trade_no = microPayRequestData.nonce_str;
        microPayRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
        microPayRequestData.auth_code = getParameter("auth_code").toString();
        MicroPay microPay = new MicroPay(microPayRequestData);
        microPay.execute(getParameter("appsecret").toString());

        return AjaxActionComplete();
    }

    public String scanPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
        unifiedOrderRequestData.appid = getParameter("appid").toString();
        unifiedOrderRequestData.mch_id = getParameter("mch_id").toString();
        unifiedOrderRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
        unifiedOrderRequestData.body = getParameter("productBody").toString();
        unifiedOrderRequestData.out_trade_no = unifiedOrderRequestData.nonce_str;
        unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
        unifiedOrderRequestData.product_id = getParameter("auth_code").toString();
        unifiedOrderRequestData.trade_type = "NATIVE";
        unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.SCANPAYCALLBACK;
        UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
        Map map = new HashMap();
        if (unifiedOrder.execute(getParameter("appsecret").toString())) {
            map.put("code_url", unifiedOrder.getCodeUrl());
        }
        return AjaxActionComplete(map);
    }

    public void perPay() throws IOException {
        String appid = getParameter("appid").toString();
        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "index.jsp";
        String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                appid, redirect_uri, appid);
        System.out.println(perPayUri);
        getResponse().sendRedirect(perPayUri);
    }

    public String brandWCPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        String appsecret = getParameter("appsecret").toString();
        UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
        unifiedOrderRequestData.appid = getParameter("state").toString();
        unifiedOrderRequestData.mch_id = getParameter("mch_id").toString();
        unifiedOrderRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
        unifiedOrderRequestData.body = getParameter("productBody").toString();
        unifiedOrderRequestData.out_trade_no = unifiedOrderRequestData.nonce_str;
        unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
        unifiedOrderRequestData.trade_type = "JSAPI";
        unifiedOrderRequestData.openid = OAuth2.fetchOpenid(unifiedOrderRequestData.appid, appsecret, getParameter("code").toString());
        unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.BRANDWCPAYCALLBACK;
        UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
        Map map = new HashMap();
        if (unifiedOrder.execute(appsecret)) {
            map.put("appId", unifiedOrderRequestData.appid);
            map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            map.put("nonceStr", StringUtils.generateRandomString(32));
            map.put("package", "prepay_id=" + unifiedOrder.getPrepayID());
            map.put("signType", "MD5");
            map.put("paySign", Signature.generateSign(map, appsecret));
        }
        return AjaxActionComplete(map);
    }
}
