package com.api.action;

import com.database.merchant.SubMerchantUser;
import com.database.weixin.OrderInfo;
import com.database.weixin.OrderInfoCollect;
import com.database.weixin.SubMerchantInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.base.Readconfig;
import com.framework.utils.MD5;
import net.sf.json.JSONObject;

import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialAction extends AjaxActionSupport {
    public String OrderInert() throws Exception {
        String data = getParameter("data").toString();
        String sign = getParameter("sign").toString();
        String id = getParameter("roleid").toString();
        //String rtn = getParameter("rtn").toString();
        String key = Readconfig.fetchspecialinfo(id);
        if (!MD5.md5LowerCase(data,key).equals(sign))
            return AjaxActionComplete(false);
        try {
            JSONObject jsonObject = JSONObject.fromObject(data);
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoBySubId(jsonObject.getString("sub_mch_id"));
            SubMerchantUser tempsubMerchantUser = new SubMerchantUser();
            tempsubMerchantUser.setSubMerchantId(subMerchantInfo.getId());
            tempsubMerchantUser.setUserName("001");
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserBySubMerchantIdAndUserName(tempsubMerchantUser);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setAppid(jsonObject.get("appid").toString());
            orderInfo.setMchId(jsonObject.get("mch_id").toString());
            orderInfo.setSubMchId(jsonObject.get("sub_mch_id").toString());
            orderInfo.setBody(subMerchantUser.getStoreName());
            orderInfo.setTransactionId(jsonObject.get("transaction_id").toString());
            orderInfo.setOutTradeNo(jsonObject.get("out_trade_no").toString());
            orderInfo.setBankType(jsonObject.get("bank_type").toString());
            orderInfo.setTotalFee(Integer.parseInt(jsonObject.get("total_fee").toString()));
            orderInfo.setTimeEnd(jsonObject.get("time_end").toString());
            orderInfo.setCreateUser(subMerchantUser.getId());//
            orderInfo.setOpenId(jsonObject.get("openid").toString());
            OrderInfo.insertOrderInfo(orderInfo);
        }
        catch (Exception e){
            return AjaxActionComplete(false);
        }
        return AjaxActionComplete(true);
    }
}
