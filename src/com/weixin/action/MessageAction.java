package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.AccessToken;
import com.weixin.api.TemplateMessage;
import com.weixin.api.RequestData.TemplateMessageRequestData;
import com.weixin.api.UserInfo;
import com.weixin.database.MerchantInfo;
import com.weixin.database.OrderInfo;

import java.util.LinkedHashMap;

public class MessageAction extends AjaxActionSupport {
    public String sendTemplateMessage() throws Exception {
        String id = getParameter("id").toString();
        String transactionId = getParameter("transactionId").toString();
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(id));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    TemplateMessageRequestData templateMessageRequestData = new TemplateMessageRequestData();
                    templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                    templateMessageRequestData.template_id = subMerchantInfo.getTemplateId();
                    OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
                    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
                    LinkedHashMap<String, String> value = new LinkedHashMap<>();
                    UserInfo userInfo = new UserInfo(accessToken, orderInfo.getOpenId());
                    if (userInfo.getRequest()) {
                        value.put("value", userInfo.getNickname());
                    }
                    else {
                        value.put("value", "匿名消费者");
                    }
                    value.put("color", "#173177");
                    body.put("first", value);
                    value = new LinkedHashMap<>();
                    value.put("value", orderInfo.getTimeEnd());
                    value.put("color", "#173177");
                    body.put("keyword1", value);
                    value = new LinkedHashMap<>();
                    value.put("value", new Integer(orderInfo.getTotalFee()).toString());
                    value.put("color", "FF0FFF");
                    body.put("keyword2", value);
                    value = new LinkedHashMap<>();
                    value.put("value", subMerchantUser.getStoreName());
                    value.put("color", "#173177");
                    body.put("keyword3", value);
                    value = new LinkedHashMap<>();
                    value.put("value", "详情请查询微信");
                    value.put("color", "#173177");
                    body.put("remark", value);
                    templateMessageRequestData.body = body;
                    TemplateMessage templateMessage = new TemplateMessage(accessToken);
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
            }
        }
        return AjaxActionComplete();
    }
}
