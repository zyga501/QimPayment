package com.api.action;

import com.database.merchant.OrderInfoCollect;
import com.database.weixin.OrderInfo;
import com.framework.action.AjaxActionSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAction extends AjaxActionSupport {
    public String CashInfo() {
        OrderInfoCollect orderInfo;
        if (null!=getParameter("mode") && (getParameter("mode").toString().equals("jdpay"))){
            orderInfo =com.database.jdpay.OrderInfoCollect.collectJDOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else if (null!=getParameter("mode") && (getParameter("mode").toString().equals("alipay"))){
            orderInfo = com.database.alipay.OrderInfoCollect.collectAliOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else {
            orderInfo = com.database.weixin.OrderInfoCollect.collectOrderInfoByDate(getParameter("id").toString(), getParameter("startdate").toString(), getParameter("enddate").toString());
        }
        Map<String, String> map = new HashMap<>();
        {
            map.put("cnt", String.valueOf(orderInfo==null?0:orderInfo.getInfoCount()));
            map.put("sumfee", String.valueOf(orderInfo==null?0:orderInfo.getSumFee()));
        }
        return AjaxActionComplete(map);
    }

    public String OrderList() {
       List<HashMap> orderInfo ;
        if (null!=getParameter("mode") && (getParameter("mode").toString().equals("jdpay"))){
            orderInfo = com.database.jdpay.OrderInfo.getJDOrderExpListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else if (null!=getParameter("mode") && (getParameter("mode").toString().equals("alipay"))){
            orderInfo = com.database.alipay.OrderInfo.getAliOrderExpListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else {
            orderInfo = OrderInfo.getOrderExpListByDate(getParameter("id").toString(), getParameter("startdate").toString(), getParameter("enddate").toString());
        }
        Map<String , Object> map = new HashMap<>();
        map.put("resultlist",orderInfo);
        return AjaxActionComplete(map);
    }

}
