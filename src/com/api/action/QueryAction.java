package com.api.action;

import com.database.alipay.AliOrderInfo;
import com.database.alipay.AliOrderInfoCollect;
import com.database.jdpay.JdOrderInfo;
import com.database.jdpay.JdOrderInfoCollect;
import com.database.merchant.OrderInfoCollect;
import com.database.weixin.WxOrderInfo;
import com.database.weixin.WxOrderInfoCollect;
import com.framework.action.AjaxActionSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAction extends AjaxActionSupport {
    public String CashInfo() {
        OrderInfoCollect orderInfo;
        if (null!=getParameter("mode") && (getParameter("mode").toString().equals("jdpay"))){
            orderInfo = JdOrderInfoCollect.collectJDOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else if (null!=getParameter("mode") && (getParameter("mode").toString().equals("alipay"))){
            orderInfo = AliOrderInfoCollect.collectAliOrderInfoByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else {
            orderInfo = WxOrderInfoCollect.collectOrderInfoByDate(getParameter("id").toString(), getParameter("startdate").toString(), getParameter("enddate").toString());
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
            orderInfo = JdOrderInfo.getJDOrderExpListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else if (null!=getParameter("mode") && (getParameter("mode").toString().equals("alipay"))){
            orderInfo = AliOrderInfo.getAliOrderExpListByDate(getParameter("id").toString(),getParameter("startdate").toString(),getParameter("enddate").toString());
        }
        else {
            orderInfo = WxOrderInfo.getOrderExpListByDate(getParameter("id").toString(), getParameter("startdate").toString(), getParameter("enddate").toString());
        }
        Map<String , Object> map = new HashMap<>();
        map.put("resultlist",orderInfo);
        return AjaxActionComplete(map);
    }

}
