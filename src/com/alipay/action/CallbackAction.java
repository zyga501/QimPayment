package com.alipay.action;

import com.database.alipay.OrderInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import net.sf.json.JSONObject;

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
        return OrderInfo.insertOrderInfo(orderInfo);
    }
}
