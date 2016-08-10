package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import com.database.merchant.SubMerchantUser;
import com.weixin.api.OrderQuery;
import com.weixin.api.RequestData.OrderQueryData;
import com.database.weixin.WxMerchantInfo;
import com.database.weixin.WxOrderInfo;
import com.database.weixin.WxSubMerchantInfo;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderAction extends AjaxActionSupport {
    public String queryOrder() throws Exception {
        com.message.WeixinMessage.sendTemplateMessage("4002382001201608100998287350");
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    OrderQueryData orderQueryData = new OrderQueryData();
                    orderQueryData.appid = merchantInfo.getAppid();
                    orderQueryData.mch_id = merchantInfo.getMchId();
                    orderQueryData.sub_mch_id = subMerchantInfo.getSubId();
                    if (getParameter("transaction_id") != null) {
                        orderQueryData.transaction_id = getParameter("transaction_id").toString();
                    }
                    if (getParameter("out_trade_no") != null) {
                        orderQueryData.out_trade_no = getParameter("out_trade_no").toString();
                    }
                    OrderQuery orderQuery = new OrderQuery(orderQueryData);
                    if (!orderQuery.postRequest(merchantInfo.getApiKey())) {
                        Logger.warn("QueryOrder Failed!");
                        return AjaxActionComplete();
                    }

                    Map<String, String> map = new HashMap<>();
                    if (null != orderQuery.getResponseResult().get("attach")) {
                        try {
                            JSONObject jsonObject = JSONObject.fromObject(orderQuery.getResponseResult().get("attach").toString());
                            if (jsonObject.get("body") != null) {
                                map.put("body", jsonObject.get("body").toString());
                            } else {
                                map.put("body", orderQuery.getResponseResult().get("attach").toString());
                            }
                        }
                        catch(Exception e){
                            map.put("body", "");
                        }
                    }
                    else {
                        map.put("body", "");
                    }
                    if (orderQuery.getResponseResult().get("transaction_id") != null) {
                        map.put("transaction_id", orderQuery.getResponseResult().get("transaction_id").toString());
                    }
                    if (orderQuery.getResponseResult().get("out_trade_no") != null) {
                        map.put("out_trade_no", orderQuery.getResponseResult().get("out_trade_no").toString());
                    }
                    if (orderQuery.getResponseResult().get("bank_type") != null) {
                        map.put("bank_type", orderQuery.getResponseResult().get("bank_type").toString());
                    }
                    if (orderQuery.getResponseResult().get("total_fee") != null) {
                        map.put("total_fee", orderQuery.getResponseResult().get("total_fee").toString());
                    }
                    if (orderQuery.getResponseResult().get("time_end") != null) {
                        map.put("time_end", orderQuery.getResponseResult().get("time_end").toString());
                    }
                    if (orderQuery.getResponseResult().get("trade_state") != null) {
                        map.put("trade_state", orderQuery.getResponseResult().get("trade_state").toString());
                    }
                    return AjaxActionComplete(map);
                }
            }
        }

        return AjaxActionComplete();
    }

    public String insertOrder() throws Exception {
        try {
            WxOrderInfo orderInfo = new WxOrderInfo();
            orderInfo.setAppid("0xFFFFFF");
            orderInfo.setMchId(getParameter("mch_id").toString());
            orderInfo.setSubMchId(getParameter("sub_mch_id").toString());
            orderInfo.setBody(getParameter("body").toString());
            if (getParameter("transaction_id") != null) {
                if (WxOrderInfo.getOrderInfoByTransactionId(getParameter("transaction_id").toString()) != null) {
                    return AjaxActionComplete(false);
                }
                orderInfo.setTransactionId(getParameter("transaction_id").toString());
            }
            if (getParameter("out_trade_no") != null) {
                orderInfo.setTransactionId(getParameter("out_trade_no").toString());
            }
            orderInfo.setBankType(getParameter("bank_type").toString());
            orderInfo.setTotalFee(Integer.parseInt(getParameter("total_fee").toString()));
            orderInfo.setTimeEnd(getParameter("time_end").toString());
            orderInfo.setOpenId("0xFFFFFF");
            orderInfo.setCreateUser(0xFFFFFF);
            WxOrderInfo.insertOrderInfo(orderInfo);
            return AjaxActionComplete(true);
        }
        catch (Exception exception) {
            return AjaxActionComplete(false);
        }
    }

    public String invalidQuery() {
        return AjaxActionComplete(false);
    }
}
