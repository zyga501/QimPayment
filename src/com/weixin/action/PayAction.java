package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.weixin.api.MicroPay;
import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.UnifiedOrderRequestData;
import com.weixin.api.UnifiedOrder;
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

    public String brandWCPay() throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        UnifiedOrderRequestData unifiedOrderRequestData = new UnifiedOrderRequestData();
        unifiedOrderRequestData.appid = getParameter("appid").toString();
        unifiedOrderRequestData.mch_id = getParameter("mch_id").toString();
        unifiedOrderRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
        unifiedOrderRequestData.body = getParameter("productBody").toString();
        unifiedOrderRequestData.out_trade_no = unifiedOrderRequestData.nonce_str;
        unifiedOrderRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
        unifiedOrderRequestData.trade_type = "JSAPI";
        unifiedOrderRequestData.openid = getParameter("openid").toString();
        unifiedOrderRequestData.notify_url = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + CallbackAction.BRANDWCPAYCALLBACK;
        UnifiedOrder unifiedOrder = new UnifiedOrder(unifiedOrderRequestData);
        Map map = new HashMap();
        if (unifiedOrder.execute(getParameter("appsecret").toString())) {
            map.put("prepay_id", unifiedOrder.getCodeUrl());
        }
        return AjaxActionComplete(map);
    }
}
