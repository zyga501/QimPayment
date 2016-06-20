package com.jdpay.action;

import com.database.jdpay.OrderInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.MD5;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.message.WeixinMessage.sendJDTemplateMessage;

public class CallbackAction extends AjaxActionSupport {
    public final static String CODEPAY = "Callback!codePay";
    public final static String H5PAY = "Callback!h5pay";

    public String codePay() throws Exception {
        handlerCallback();
        return AjaxActionComplete();
    }

    public String h5pay() throws Exception {
        if (handlerCallback())
            ResponseWrite("success");
        return AjaxActionComplete();
    }

    private boolean handlerCallback() throws Exception {
        {
            String data = "";
            if (null != getParameter("DATA")) {
                System.out.println("DATA:" + getParameter("DATA").toString());
                data =getParameter("DATA").toString();
                data = data.replace("\n","");
                data =   new String(Base64.getDecoder().decode(data),"utf8");
                System.out.println("data:" + data);
            }//对data作签名和sign对比，此步骤省略.
            JSONObject jsonObject = JSONObject.fromObject(data);
            Map map =new HashMap();
            map.put("amount",jsonObject.get("amount"));
            map.put("merchant_no",jsonObject.get("merchant_no"));
            map.put("order_no",jsonObject.get("order_no"));
            map.put("pay_time",jsonObject.get("pay_time"));
            map.put("promotionAmount",jsonObject.get("promotionAmount"));
            map.put("sub_mer",jsonObject.get("sub_mer"));
            map.put("term_no",null==jsonObject.get("term_no")?jsonObject.get("extra_info"):jsonObject.get("term_no"));
            map.put("trade_no",jsonObject.get("trade_no"));
            map.put("user",jsonObject.get("user"));
            if ( saveOrderToDb(map))
                sendJDTemplateMessage(map.get("trade_no").toString());
            return true;
        }
    }

    public static boolean saveOrderToDb(Map map){
        return OrderInfo.insertOrderInfo(map);
    }
}