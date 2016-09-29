package pf.jdpay.action;

import pf.database.jdpay.JdOrderInfo;
import framework.action.AjaxActionSupport;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static pf.message.NotifyCenter.NotifyMessage;
import static pf.message.WeixinMessage.sendJDTemplateMessage;

public class CallbackAction extends AjaxActionSupport {
    public final static String CODEPAY = "Callback!codePay";
    public final static String H5PAY = "Callback!h5pay";

    public String codePay() throws Exception {
        handlerCallback(1);
        return AjaxActionComplete();
    }

    public String h5pay() throws Exception {
        if (handlerCallback(2))
            ResponseWrite("success");
        return AjaxActionComplete();
    }

    private boolean handlerCallback(int typeid) throws Exception {
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
            map.put("term_no",null==jsonObject.get("term_no")?jsonObject.get("extra_info"):jsonObject.get("term_no"));//存放userid
            map.put("trade_no",jsonObject.get("trade_no"));
            map.put("user",jsonObject.get("user"));
            if ( saveOrderToDb(map)) {
                sendJDTemplateMessage(map.get("trade_no").toString());
                System.out.println("jdmsg over");
                if (typeid==2)
                notifyClientToPrint(map);
                System.out.println("remote jd");
            }
            return true;
        }
    }
    private void notifyClientToPrint(Map<String,Object> responseResult) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("body", responseResult.get("sub_mer")==null? "*":responseResult.get("sub_mer").toString());
        map.put("transaction_id",responseResult.get("trade_no").toString());
        map.put("out_trade_no", responseResult.get("order_no").toString());
        map.put("bank_type","*");
        map.put("total_fee", responseResult.get("amount").toString());
        map.put("time_end", responseResult.get("pay_time").toString());
        NotifyMessage(Long.parseLong(responseResult.get("term_no").toString()),responseResult.get("term_no").toString().concat("#jdpay@").concat(JSONObject.fromObject(map).toString()));
    }

    public static boolean saveOrderToDb(Map map){
        return JdOrderInfo.insertOrderInfo(map);
    }
}