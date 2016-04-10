package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.XMLParser;
import com.weixin.database.MerchantInfo;
import com.weixin.database.OrderInfo;
import com.weixin.utils.Signature;
import net.sf.json.JSONObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";

    public void scanPay() throws IOException, ParserConfigurationException, IOException, SAXException {
        handlerCallback();
    }

    public void brandWCPay() throws IOException, ParserConfigurationException, IOException, SAXException {
        handlerCallback();
    }

    private boolean handlerCallback() throws IOException, ParserConfigurationException, IOException, SAXException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer = null;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }

        String responseString = stringBuilder.toString();
        Map<String,Object> responseResult = XMLParser.convertMapFromXML(responseString);;
        MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoByAppId(responseResult.get("appid").toString());
        if (merchantInfo != null) {
            if (!Signature.checkSignValid(responseResult, merchantInfo.getApiKey())) {
                System.out.println("checkSignValid Failed!");
                return false;
            }
        }

        return saveOrderToDb(responseResult);
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
        String transactionId = responseResult.get("transaction_id").toString();
        OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo != null) {
            return true;
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
        return OrderInfo.insertOrderInfo(orderInfo);
    }
}