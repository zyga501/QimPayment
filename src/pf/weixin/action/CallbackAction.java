package pf.weixin.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.HttpUtils;
import QimCommon.utils.StringUtils;
import QimCommon.utils.XMLParser;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxOrderInfo;
import pf.message.NotifyCenter;
import pf.message.WeixinMessage;
import pf.weixin.utils.Signature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";
    public final static String WEIXINCALLBACKSUCCESS = "" +
            "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    public final static Object syncObject = new Object();

    public void scanPay() throws Exception {
        handlerCallback(1);
        getResponse().getWriter().write(WEIXINCALLBACKSUCCESS);
    }

    public void brandWCPay() throws Exception {
        handlerCallback(2);
        getResponse().getWriter().write(WEIXINCALLBACKSUCCESS);
    }

    private boolean handlerCallback(int typeid) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();

        String responseString = stringBuilder.toString();
        Map<String,Object> responseResult = XMLParser.convertMapFromXml(responseString);;
        WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoByAppId(responseResult.get("appid").toString());
        if (merchantInfo != null) {
            if (!Signature.checkSignValid(responseResult, merchantInfo.getApiKey())) {
                ProjectLogger.warn(this.getClass().getName() + " CheckSignValid Failed!");
                ProjectLogger.error(this.getClass().getName() + " " + responseString);
                return false;
            }
        }

        JSONObject jsonObject = JSONObject.fromObject(responseResult.get("attach").toString());
        responseResult.put("id", jsonObject.get("id").toString());
        responseResult.put("body", jsonObject.get("body").toString());
        responseResult.put("redirect_uri", StringUtils.convertNullableString(jsonObject.get("redirect_uri")));
        responseResult.put("data", jsonObject.get("data").toString());

        boolean ret = saveOrderToDb(responseResult);
        if (ret) {
            notifyClientOrderInfo(responseResult);
            WeixinMessage.sendTemplateMessage(responseResult.get("transaction_id").toString());
            if (typeid==2)
                notifyClientToPrint(responseResult);
            return true;
        }

        return false;
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
        synchronized (syncObject) {
            String transactionId = responseResult.get("transaction_id").toString();
            WxOrderInfo orderInfo = WxOrderInfo.getOrderInfoByTransactionId(transactionId);
            if (orderInfo != null) {
                return false;
            }

            orderInfo = new WxOrderInfo();
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
            return WxOrderInfo.insertOrderInfo(orderInfo);
        }
    }

    private void notifyClientToPrint(Map<String,Object> responseResult) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("body", responseResult.get("body").toString());
        map.put("transaction_id",responseResult.get("transaction_id").toString());
        map.put("out_trade_no", responseResult.get("out_trade_no").toString());
        map.put("bank_type", responseResult.get("bank_type").toString());
        map.put("total_fee", responseResult.get("total_fee").toString());
        map.put("time_end", responseResult.get("time_end").toString());
        NotifyCenter.NotifyMessage(Long.parseLong(responseResult.get("id").toString()), responseResult.get("id").toString().concat("#weixin@").concat(JSONObject.fromObject(map).toString()));
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
            map.put("data", responseResult.get("data").toString());
            HttpPost httpPost = new HttpPost(redirect_uri);
            StringEntity postEntity = new StringEntity(JSONObject.fromObject(map).toString(), "UTF-8");
            httpPost.addHeader("Content-Type", "text/json");
            httpPost.setEntity(postEntity);

            String responseString = new String();
            try {
                responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse)->
                {
                    return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                });
            }
            finally {
                httpPost.abort();
            }
        }
    }
}