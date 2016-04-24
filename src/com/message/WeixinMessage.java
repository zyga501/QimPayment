package com.message;

import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.database.weixin.OrderInfo;
import com.weixin.api.AccessToken;
import com.weixin.api.RequestData.TemplateMessageRequestData;
import com.weixin.api.TemplateMessage;
import com.weixin.api.UserInfo;

public class WeixinMessage {
    public static boolean sendTemplateMessage(String transactionId) throws Exception {
        OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo == null) {
            return false;
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(orderInfo.getCreateUser());
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            String accessToken = new String();
            if (subMerchantInfo != null) {
                com.database.weixin.SubMerchantInfo subMerchantWeixinInfo = com.database.weixin.SubMerchantInfo.getSubMerchantInfoById(subMerchantInfo.getId());
                if (!subMerchantWeixinInfo.getAppid().isEmpty() && subMerchantWeixinInfo.getAppsecret().isEmpty()) {
                    accessToken = AccessToken.getAccessToken(subMerchantWeixinInfo.getAppid());
                }
                else {
                    MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    }
                }
            }

            if (!accessToken.isEmpty()) {
                TemplateMessageRequestData templateMessageRequestData = new TemplateMessageRequestData();
                templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                templateMessageRequestData.template_id = subMerchantInfo.getTemplateId();
                UserInfo userInfo = new UserInfo(accessToken, orderInfo.getOpenId());
                if (userInfo.getRequest()) {
                    templateMessageRequestData.nickName = userInfo.getNickname();
                }
                else {
                    templateMessageRequestData.nickName = "匿名消费者";
                }
                templateMessageRequestData.timeEnd = orderInfo.getTimeEnd();
                templateMessageRequestData.totalFee = orderInfo.getTotalFee() / 100.0;
                templateMessageRequestData.storeName = subMerchantUser.getStoreName();
                templateMessageRequestData.transactionId = transactionId;
                TemplateMessage templateMessage = new TemplateMessage(accessToken);
                String requestData = templateMessageRequestData.buildRequestData();
                if (!requestData.isEmpty() && templateMessage.postRequest(requestData)) {
                    templateMessageRequestData.touser = subMerchantInfo.getWeixinId();
                    return templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
            }
        }

        return false;
    }
}
