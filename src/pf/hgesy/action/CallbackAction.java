package pf.hgesy.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.HttpUtils;
import QimCommon.utils.SessionCache;
import QimCommon.utils.StringUtils;
import QimCommon.utils.Zip;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import pf.database.hgesy.HgesyOrderInfo;

import java.util.HashMap;
import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String HGESYCALLBACKSUCCESS = "SUCCESS";
    public final static Object syncObject = new Object();

    public void directPay() throws Exception {
        Map<String, Object> responseResult = new HashMap<>();
        if (!StringUtils.convertNullableString(getParameter("order_no")).isEmpty()) {
            JSONObject jsonObject = null;
            String sessionId = getParameter("order_no").toString();
            String sesseionData = SessionCache.getSessionData(sessionId).toString();
            if (!sesseionData.isEmpty()) {
                jsonObject = JSONObject.fromObject(Zip.unZip(sesseionData));
                SessionCache.clearSessionData(sessionId);
            }
            responseResult.put("id", jsonObject.get("id").toString());
            responseResult.put("body", jsonObject.get("body").toString());
            responseResult.put("redirect_uri", StringUtils.convertNullableString(jsonObject.get("url")));
            responseResult.put("data", jsonObject.get("data").toString());
            responseResult.put("time_end", getParameter("request_time").toString());
            responseResult.put("out_trade_no", getParameter("order_no").toString());
            responseResult.put("total_fee", (int) (Double.parseDouble(getParameter("total_fee").toString()) * 100));

            synchronized (syncObject) {
                HgesyOrderInfo hgesyOrderInfo = new HgesyOrderInfo();
                hgesyOrderInfo.setMchId(Long.parseLong(responseResult.get("id").toString()));
                hgesyOrderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
                hgesyOrderInfo.setBody(responseResult.get("body").toString());
                hgesyOrderInfo.setTimeEnd(responseResult.get("time_end").toString());
                hgesyOrderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
                if (HgesyOrderInfo.insertOrderInfo(hgesyOrderInfo)) {
                    notifyClientOrderInfo(responseResult);
                }
            }

            getResponse().getWriter().write(HGESYCALLBACKSUCCESS);
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
