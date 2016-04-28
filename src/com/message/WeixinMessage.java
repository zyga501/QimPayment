package com.message;

import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.database.weixin.OrderInfo;
import com.framework.utils.StringUtils;
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
                if (!StringUtils.convertNullableString(subMerchantWeixinInfo.getAppid()).isEmpty()
                        && StringUtils.convertNullableString(subMerchantWeixinInfo.getAppsecret()).isEmpty()) {
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
                if (!StringUtils.convertNullableString(subMerchantUser.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                if (!StringUtils.convertNullableString(subMerchantInfo.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantInfo.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                return true;
            }
        }

        return false;
    }
}
