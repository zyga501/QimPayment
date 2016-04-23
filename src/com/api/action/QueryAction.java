package com.api.action;

import com.framework.action.AjaxActionSupport;
import com.database.weixin.OrderInfo;
import com.database.weixin.OrderInfoCollect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAction extends AjaxActionSupport {
    public String CashInfo() {
        OrderInfoCollect orderInfo = OrderInfoCollect.collectOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        Map<String, String> map = new HashMap<>();
        if (null!=orderInfo) {
            map.put("cnt", String.valueOf(orderInfo.getInfoCount()));
            map.put("sumfee", String.valueOf(orderInfo.getSumFee()));
        }
        return AjaxActionComplete(map);
    }

    public String OrderList() {
       List<HashMap> orderInfo = OrderInfo.getOrderExpListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        Map<String , Object> map = new HashMap<>();
        map.put("resultlist",orderInfo);
        return AjaxActionComplete(map);
    }
}
