package pf.paymind.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.*;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.ProjectLogger;
import pf.database.paymind.PmOrderInfo;

import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String JSPAYCALLBACK = "Callback!jsPay";
    public final static Object syncObject = new Object();

    public void jsPay() throws Exception {
        handlerCallback();
    }

    private void handlerCallback() throws Exception {
        Map<String, Object> responseResult = new HashMap<>();
        if (!StringUtils.convertNullableString(getParameter("r2_orderNumber")).isEmpty()) {
            JSONObject jsonObject = null;
            String sessionId = getParameter("r2_orderNumber").toString();
            String sesseionData = SessionCache.getSessionData(sessionId).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
                SessionCache.clearSessionData(sessionId);
            }
            responseResult.put("id", jsonObject.get("id").toString());
            responseResult.put("body", jsonObject.get("body").toString());
            responseResult.put("redirect_uri", StringUtils.convertNullableString(jsonObject.get("url")));
            responseResult.put("data", jsonObject.get("data").toString());
            responseResult.put("time_end", getParameter("r7_completeDate").toString());
            responseResult.put("out_trade_no", getParameter("r2_orderNumber").toString());
            responseResult.put("total_fee", (int)(Double.parseDouble(getParameter("r3_amount").toString()) * 100));

            synchronized (syncObject) {
                PmOrderInfo pmOrderInfo = new PmOrderInfo();
                pmOrderInfo.setMchId(Long.parseLong(responseResult.get("id").toString()));
                pmOrderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
                pmOrderInfo.setBody(responseResult.get("body").toString());
                pmOrderInfo.setTimeEnd(responseResult.get("time_end").toString());
                pmOrderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
                if (PmOrderInfo.insertOrderInfo(pmOrderInfo)) {
                    notifyClientOrderInfo(responseResult);
                }
            }
        }
    }

    private void notifyClientOrderInfo(Map<String, Object> responseResult) throws Exception {
        if (!StringUtils.convertNullableString(responseResult.get("redirect_uri")).isEmpty()) {
            String redirect_uri = responseResult.get("redirect_uri").toString();
            Map<String, String> map = new HashMap<>();
            map.put("body", responseResult.get("body").toString());
            map.put("out_trade_no", responseResult.get("out_trade_no").toString());
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
