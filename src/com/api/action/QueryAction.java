package com.api.action;

import com.framework.action.AjaxActionSupport;
import com.weixin.database.OrderInfo;
import com.weixin.database.OrderInfoCollect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAction extends AjaxActionSupport {
    public String CashInfo(){
        OrderInfoCollect orderInfo = OrderInfoCollect.collectOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        Map<String, String> map = new HashMap<>();
        if (null!=orderInfo) {
            map.put("cnt", String.valueOf(orderInfo.getInfoCount()));
            map.put("sumfee", String.valueOf(orderInfo.getSumFee()));
        }
        return AjaxActionComplete(map);
    }
    public String OrderList(){
       List<OrderInfo> orderInfo = OrderInfo.getOrderInfoListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        Map<String , Object> map = new HashMap<>();
        map.put("resultlist",orderInfo);
        return AjaxActionComplete(map);
    }
}
