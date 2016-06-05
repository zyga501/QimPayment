package com.jdpay.action;


import com.database.jdpay.MerchantInfo;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;
import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;
import com.jdpay.api.OrderQuery;
import com.jdpay.api.RequestData.MicroPayRequestData;
import com.jdpay.api.RequestData.QueryRequestData;
import com.jdpay.api.RequestData.TokenPayRequestData;
import com.jdpay.api.MicroPay;
import com.jdpay.api.TokenOrder;

import java.util.HashMap;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String microPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
                if (merchantInfo != null) {
                    MicroPayRequestData microPayRequestData = new MicroPayRequestData();
                    microPayRequestData.order_no = getParameter("orderno").toString();
                    microPayRequestData.seed =  getParameter("seed").toString();
                    microPayRequestData.merchant_no = merchantInfo.getPaycodemerchantno();
                    microPayRequestData.order_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "jdpay/"
                            + CallbackAction.CODEPAY;;
                    microPayRequestData.notify_url = requestUrl;
                    microPayRequestData.amount = Double.parseDouble(getParameter("price").toString());//0.01
                    microPayRequestData.trade_name =  getParameter("goodsname").toString();
                    microPayRequestData.trade_describle =  getParameter("goodsmemo").toString();
                    microPayRequestData.sub_mer =  getParameter("storename").toString();
                    microPayRequestData.term_no = subMerchantUser.getUserName();
                    MicroPay microPay = new MicroPay(microPayRequestData);
                    if (!microPay.postRequest(merchantInfo.getPaycodemd5key())) {
                        return AjaxActionComplete(false);
                    } else {
                        return AjaxActionComplete(microPay.getResponseResult());
                    }
                }
            }
        }
        return AjaxActionComplete(false);
    }

    public String tokenPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
                if (merchantInfo != null) {
                    TokenPayRequestData tokenPayRequestData = new TokenPayRequestData();
                    tokenPayRequestData.expire =  5;
                    tokenPayRequestData.merchant_no = merchantInfo.getScanmerchantno();
                    tokenPayRequestData.order_no =  StringUtils.convertNullableString(getParameter("orderno"),String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId()));
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "jdpay/"
                            + CallbackAction.CODEPAY;;
                    tokenPayRequestData.notify_url = requestUrl;
                    tokenPayRequestData.amount =Double.parseDouble(getParameter("price").toString());
                    tokenPayRequestData.trade_name = StringUtils.convertNullableString(getParameter("goodsname"), subMerchantUser.getStoreName());
                    if (null!=getParameter("goodsmemo"))
                    tokenPayRequestData.trade_describle =   StringUtils.convertNullableString(getParameter("goodsmemo"),"描述");
                    tokenPayRequestData.sub_mer = subMerchantUser.getStoreName();
                    tokenPayRequestData.term_no = subMerchantUser.getUserName();
                    TokenOrder tokenOrder = new TokenOrder(tokenPayRequestData);
                    if (!tokenOrder.postRequest(merchantInfo.getScanmd5key())) {
                        Logger.warn("return TokenPay code_url Failed!");
                        return AjaxActionComplete(false);
                    } else {
                        Logger.info("return TokenPay code_url Succeed!");
                        Map<String, String> map = new HashMap<>();
                        map.put("code_url",  ((Map<String,String>)(tokenOrder.getResponseResult().get("data"))).get("qrcode"));
                        return AjaxActionComplete(map);
                    }
                }
            }
        }
        return AjaxActionComplete(false);
    }

    public String queryPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
                if (merchantInfo != null) {
                    QueryRequestData queryRequestData = new QueryRequestData();
                    queryRequestData.order_no = getParameter("orderno").toString();
                    queryRequestData.merchant_no = merchantInfo.getPaycodemerchantno();
                    OrderQuery orderQuery = new OrderQuery(queryRequestData);
                    if (!orderQuery.postRequest(merchantInfo.getPaycodemd5key())) {
                        QueryRequestData queryRequestData2 = new QueryRequestData();
                        queryRequestData2.order_no = getParameter("orderno").toString();
                        queryRequestData2.merchant_no = merchantInfo.getScanmerchantno();
                        OrderQuery orderQuery2 = new OrderQuery(queryRequestData2);
                        if (orderQuery2.postRequest(merchantInfo.getScanmd5key())) {
                            System.out.println(orderQuery2.getResponseResult().get("data"));
                            return AjaxActionComplete(orderQuery2.getResponseResult());
                        } else
                            return AjaxActionComplete(false);
                    } else {
                        System.out.println(orderQuery.getResponseResult().get("data"));
                        return AjaxActionComplete(orderQuery.getResponseResult());
                    }
                }
            }
        }

        return AjaxActionComplete(false);
    }
}
