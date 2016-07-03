package com.alipay.action;

import com.database.alipay.OrderInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.message.NotifyCenter.NoiftyMessage;

public class CallbackAction extends AjaxActionSupport {
    public final static String TRADEPRECREATE = "Callback!tradePreCreate";
    public final static String SUCCESS = "success";

    public void tradePreCreate() throws Exception {
        Logger.warn("tradePreCreate Callback");
        handlerCallback();
        getResponse().getWriter().write(SUCCESS);
    }

    private boolean handlerCallback() throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAppid(getParameter("app_id").toString());
        orderInfo.setMchId(Long.parseLong(getParameter("mchId").toString()));
        orderInfo.setSubject(getParameter("subject").toString());
        orderInfo.setTradeNo(getParameter("trade_no").toString());
        orderInfo.setOutTradeNo(getParameter("out_trade_no").toString());
        String fund_bill_list = getParameter("fund_bill_list").toString().substring(1);
        fund_bill_list = fund_bill_list.substring(0, fund_bill_list.length() - 1);
        orderInfo.setFundChannel(JSONObject.fromObject(getParameter("fund_bill_list")).getString("fundChannel"));
        orderInfo.setTotalAmount(Double.parseDouble(getParameter("total_amount").toString()));
        orderInfo.setGmtPayment(getParameter("gmt_payment").toString());
        orderInfo.setCreateUser(Long.parseLong(getParameter("createUser").toString()));
        orderInfo.setOpenId(getParameter("open_id").toString());
        Map<String, String> map = new HashMap<>();
        map.put("body",getParameter("subject").toString());
        map.put("transaction_id",getParameter("trade_no").toString());
        map.put("out_trade_no", getParameter("out_trade_no").toString());
        map.put("total_fee", getParameter("total_amount").toString());
        map.put("time_end", getParameter("gmt_payment").toString());
        NoiftyMessage(Long.parseLong(getParameter("createUser").toString()),getParameter("createUser").toString().concat("#jdpay@").concat(JSONObject.fromObject(map).toString()));
        return OrderInfo.insertOrderInfo(orderInfo);
    }
}
