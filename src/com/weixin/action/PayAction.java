package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.weixin.api.MicroPay;
import com.weixin.api.RequestData.MicroPayRequestData;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws UnknownHostException, IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        MicroPayRequestData microPayRequestData = new MicroPayRequestData();
        microPayRequestData.appid = getParameter("appid").toString();
        microPayRequestData.mch_id = getParameter("mch_id").toString();
        microPayRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
        microPayRequestData.body = getParameter("productBody").toString();
        microPayRequestData.out_trade_no = microPayRequestData.nonce_str;
        microPayRequestData.total_fee = Integer.parseInt(getParameter("productFee").toString());
        microPayRequestData.spbill_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        microPayRequestData.auth_code = getParameter("auth_code").toString();
        MicroPay microPay = new MicroPay(microPayRequestData);
        microPay.execute(getParameter("appsecret").toString());

        return AjaxActionComplete();
    }
}
