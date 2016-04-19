package com.message;

import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.AccessToken;
import com.weixin.api.RequestData.TemplateMessageRequestData;
import com.weixin.api.TemplateMessage;
import com.weixin.api.UserInfo;
import com.weixin.database.MerchantInfo;
import com.weixin.database.OrderInfo;

public class WeixinMessage {
    public static boolean sendTemplateMessage(String transactionId) throws Exception {
        OrderInfo orderInfo = OrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo == null) {
            return false;
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(orderInfo.getCreateUser());
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
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
                    return templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
            }
        }

        return false;
    }
}
