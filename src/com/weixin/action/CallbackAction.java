package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import com.framework.utils.UdpSocket;
import com.framework.utils.XMLParser;
import com.message.WeixinMessage;
import com.database.weixin.MerchantInfo;
import com.database.weixin.OrderInfo;
import com.weixin.utils.Signature;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";
    public final static String WEIXINCALLBACKSUCCESS = "" +
            "<xml>" +
            "<return_code><![CDATA[SUCCESS]]></return_code>" +
            "<return_msg><![CDATA[OK]]></return_msg>" +
            "<result_code><![CDATA[SUCCESS]]></result_code>" +
            "</xml>";

    public void scanPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(WEIXINCALLBACKSUCCESS);
    }

    public void brandWCPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(WEIXINCALLBACKSUCCESS);
    }

    private boolean handlerCallback() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer = null;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();

        String responseString = stringBuilder.toString();
        Map<String,Object> responseResult = XMLParser.convertMapFromXML(responseString);;
        MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoByAppId(responseResult.get("appid").toString());
        if (merchantInfo != null) {
            if (!Signature.checkSignValid(responseResult, merchantInfo.getApiKey())) {
                Logger.warn(this.getClass().getName() + " CheckSignValid Failed!");
                return false;
            }
        }

        boolean ret = saveOrderToDb(responseResult);
        if (ret) {
            notifyClientToPrint(responseResult);
            return WeixinMessage.sendTemplateMessage(responseResult.get("transaction_id").toString());
        }

        return false;
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
        String transactionId = responseResult.get("transaction_id").toString();
        OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo != null) {
            return false;
        }
        orderInfo = new OrderInfo();
        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
        orderInfo.setAppid(responseResult.get("appid").toString());
        orderInfo.setMchId(responseResult.get("mch_id").toString());
        orderInfo.setSubMchId(responseResult.get("sub_mch_id").toString());
        orderInfo.setBody(jsonObject.get("body").toString());
        orderInfo.setTransactionId(responseResult.get("transaction_id").toString());
        orderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
        orderInfo.setBankType(responseResult.get("bank_type").toString());
        orderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
        orderInfo.setTimeEnd(responseResult.get("time_end").toString());
        orderInfo.setCreateUser(Long.parseLong(jsonObject.get("id").toString()));
        orderInfo.setOpenId(responseResult.get("openid").toString());
        return OrderInfo.insertOrderInfo(orderInfo);
    }

    private void notifyClientToPrint(Map<String,Object> responseResult) throws IOException {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
        map.put("body", jsonObject.get("body").toString());
        map.put("transaction_id",responseResult.get("transaction_id").toString());
        map.put("out_trade_no", responseResult.get("out_trade_no").toString());
        map.put("bank_type", responseResult.get("bank_type").toString());
        map.put("total_fee", responseResult.get("total_fee").toString());
        map.put("time_end", responseResult.get("time_end").toString());
        new UdpSocket("127.0.0.1", 8848).send(jsonObject.get("id").toString().concat("@").concat(JSONObject.fromObject(map).toString()).getBytes());
    }
}
