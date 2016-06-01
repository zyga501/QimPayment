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

public class CallbackAction extends AjaxActionSupport {
    public final static String CODEPAY = "Callback!codePay";

    public String codePay() throws Exception {
        handlerCallback();
        return AjaxActionComplete();
    }

    private boolean handlerCallback() throws Exception {
         {
            String data = "";
            String sign;
            if (null != getParameter("DATA")) {
                System.out.println("data:" + getParameter("DATA").toString());
                data =getParameter("DATA").toString();
                data = data.replace("\n","");
                sign = MD5.md5LowerCase(data + "bipy5w9rGn0rCGheaszUieiyIvFoKyUr", "bipy5w9rGn0rCGheaszUieiyIvFoKyUr");
                data =   new String(Base64.getDecoder().decode(data),"utf8");
                System.out.println("sign:" + sign);
            }
            if (null != getParameter("SIGN")) {
                System.out.println("SIGN:" + getParameter("SIGN").toString());
            }
            saveOrderToDb(data);
            return true;
        }
//        catch (Exception e){
//            return false;
//        }
    }

    public static boolean saveOrderToDb(String data){
        JSONObject jsonObject = JSONObject.fromObject(data);
        Map map =new HashMap();
        map.put("amount",jsonObject.get("amount"));
        map.put("merchant_no",jsonObject.get("merchant_no"));
        map.put("order_no",jsonObject.get("order_no"));
        map.put("pay_time",jsonObject.get("pay_time"));
        map.put("promotionAmount",jsonObject.get("promotionAmount"));
        map.put("sub_mer",jsonObject.get("sub_mer"));
        map.put("term_no",jsonObject.get("term_no"));
        map.put("trade_no",jsonObject.get("trade_no"));
        map.put("user",jsonObject.get("user"));
        return OrderInfo.insertOrderInfo(map);
    }
}
