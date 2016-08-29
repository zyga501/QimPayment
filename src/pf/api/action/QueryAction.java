package pf.api.action;

import pf.database.alipay.AliOrderInfo;
import pf.database.alipay.AliOrderInfoCollect;
import pf.database.jdpay.JdOrderInfo;
import pf.database.jdpay.JdOrderInfoCollect;
import pf.database.merchant.OrderInfoCollect;
import pf.database.weixin.WxOrderInfo;
import pf.database.weixin.WxOrderInfoCollect;
import pf.framework.action.AjaxActionSupport;

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
