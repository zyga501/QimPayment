package pf.swiftpass.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.*;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.database.swiftpass.SwiftOrderInfo;

import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String WEIXINJSPAYCALLBACK = "Callback!weixinJsPay";
    public final static String ALIJSPAYCALLBACK = "Callback!aliJsPay";
    public final static String SUCCESS = "success";
    public final static Object syncObject = new Object();

    public void weixinJsPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(SUCCESS);
    }

    public void aliJsPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(SUCCESS);
    }

    private void handlerCallback() throws Exception {
        Map<String,Object> responseResult = XMLParser.convertMapFromXml(getInputStreamAsString());
        if (responseResult.get("result_code").toString().compareTo("0") == 0 &&
                responseResult.get("pay_result").toString().compareTo("0") == 0) {
            JSONObject jsonObject = null;
            String sesseionData = SessionCache.getSessionData(responseResult.get("attach").toString()).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
                SessionCache.clearSessionData(sesseionData);
            }
            responseResult.put("id", jsonObject.get("id").toString());
            responseResult.put("body", jsonObject.get("body").toString());
            responseResult.put("redirect_uri", StringUtils.convertNullableString(jsonObject.get("url")));
            responseResult.put("data", jsonObject.get("data").toString());

            synchronized (syncObject) {
                String tradeNo = StringUtils.convertNullableString(responseResult.get("out_trade_no"));
                if (SwiftOrderInfo.getOrderInfoByOrderNo(tradeNo) == null) {
                    SwiftOrderInfo swiftOrderInfo = new SwiftOrderInfo();
                    swiftOrderInfo.setMchId(Long.parseLong(responseResult.get("id").toString()));
                    swiftOrderInfo.setOutTradeNo(tradeNo);
                    swiftOrderInfo.setBody(responseResult.get("body").toString());
                    swiftOrderInfo.setTimeEnd(responseResult.get("time_end").toString());
                    swiftOrderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
                    if (SwiftOrderInfo.insertOrderInfo(swiftOrderInfo)) {
                        notifyClientOrderInfo(responseResult);
                    }
                }
            }
            return;
        }

        ProjectLogger.error("Swiftpass Callback Error!");
    }

    private void notifyClientOrderInfo(Map<String, Object> responseResult) throws Exception {
        ProjectLogger.info("nofityClientUrl:" + StringUtils.convertNullableString(responseResult.get("redirect_uri")));
        if (!StringUtils.convertNullableString(responseResult.get("redirect_uri")).isEmpty()) {
            String redirect_uri = responseResult.get("redirect_uri").toString();
            Map<String, String> map = new HashMap<>();
            map.put("body", responseResult.get("body").toString());
            map.put("transaction_id",responseResult.get("transaction_id").toString());
            map.put("out_trade_no", responseResult.get("out_trade_no").toString());
            map.put("bank_type", "*");
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
