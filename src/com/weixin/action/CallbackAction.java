package com.weixin.action;

import com.database.weixin.MerchantInfo;
import com.database.weixin.OrderInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.*;
import com.message.WeixinMessage;
import com.weixin.utils.Signature;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";
    public final static String WEIXINCALLBACKSUCCESS = "" +
            "SUCCESS";

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
        String lineBuffer;
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

        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
        responseResult.put("id", jsonObject.get("id").toString());
        responseResult.put("body", jsonObject.get("body").toString());
        responseResult.put("redirect_uri", StringUtils.convertNullableString(jsonObject.get("redirect_uri")));

        boolean ret = saveOrderToDb(responseResult);
        if (ret) {
            notifyClientToPrint(responseResult);
            WeixinMessage.sendTemplateMessage(responseResult.get("transaction_id").toString());
            notifyClientOrderInfo(responseResult);
            return true;
        }

        return false;
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
        String transactionId = responseResult.get("transaction_id").toString();
        OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo != null) {
            return false;
        }

        Logger.info(responseResult.get("attach").toString());
        orderInfo = new OrderInfo();
        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
        orderInfo.setAppid(responseResult.get("appid").toString());
        orderInfo.setMchId(responseResult.get("mch_id").toString());
        orderInfo.setSubMchId(responseResult.get("sub_mch_id").toString());
        orderInfo.setBody(responseResult.get("body").toString());
        orderInfo.setTransactionId(responseResult.get("transaction_id").toString());
        orderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
        orderInfo.setBankType(responseResult.get("bank_type").toString());
        orderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
        orderInfo.setTimeEnd(responseResult.get("time_end").toString());
        orderInfo.setCreateUser(Long.parseLong(responseResult.get("id").toString()));
        orderInfo.setOpenId(responseResult.get("openid").toString());
        return OrderInfo.insertOrderInfo(orderInfo);
    }

    private void notifyClientToPrint(Map<String,Object> responseResult) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("body", responseResult.get("body").toString());
        map.put("transaction_id",responseResult.get("transaction_id").toString());
        map.put("out_trade_no", responseResult.get("out_trade_no").toString());
        map.put("bank_type", responseResult.get("bank_type").toString());
        map.put("total_fee", responseResult.get("total_fee").toString());
        map.put("time_end", responseResult.get("time_end").toString());
        new UdpSocket("127.0.0.1", 8848).send(responseResult.get("id").toString().concat("@").concat(JSONObject.fromObject(map).toString()).getBytes());
    }

    private void notifyClientOrderInfo(Map<String, Object> responseResult) throws Exception {
        if (!StringUtils.convertNullableString(responseResult.get("redirect_uri")).isEmpty()) {
            String redirect_uri = responseResult.get("redirect_uri").toString();
            Map<String, String> map = new HashMap<>();
            map.put("body", responseResult.get("body").toString());
            map.put("transaction_id",responseResult.get("transaction_id").toString());
            map.put("out_trade_no", responseResult.get("out_trade_no").toString());
            map.put("bank_type", responseResult.get("bank_type").toString());
            map.put("total_fee", responseResult.get("total_fee").toString());
            map.put("time_end", responseResult.get("time_end").toString());
            HttpPost httpPost = new HttpPost(redirect_uri);
            StringEntity postEntity = new StringEntity(JSONObject.fromObject(map).toString(), "UTF-8");
            httpPost.addHeader("Content-Type", "text/json");
            httpPost.setEntity(postEntity);

            String responseString = new String();
            try {
                responseString = HttpUtils.PostRequest(httpPost, (HttpEntity httpEntity)->
                {
                    return EntityUtils.toString(httpEntity, "UTF-8");
                });
            }
            finally {
                httpPost.abort();
            }
        }
    }
}