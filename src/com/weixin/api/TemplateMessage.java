package com.weixin.api;

import com.framework.utils.Logger;
import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.RequestData.TemplateMessageRequestData;
import com.weixin.database.MerchantInfo;
import com.weixin.database.OrderInfo;
import net.sf.json.JSONObject;

public class TemplateMessage extends WeixinAPI {
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    public static boolean sendMessage(String transactionId) throws Exception {
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

    public TemplateMessage(String accessToken) {
        accessToken_ = accessToken;
    }

    @Override
    protected String getAPIUri() {
        return String.format(SEND_MESSAGE, accessToken_);
    }

    @Override
    protected boolean handlerResponse(String responseResult) throws Exception {
        JSONObject jsonParse = JSONObject.fromObject(responseResult);
        if (jsonParse.get("errorcode") != null) {
            String errorCode = jsonParse.get("errorcode").toString();
            if (errorCode.compareTo("40001") == 0) {
                String appid = AccessToken.getAppidByAccessToken(accessToken_);
                if (appid.isEmpty()) {
                    Logger.error("AccessToken Handler Error!");
                    return false;
                }

                accessToken_ = AccessToken.updateAccessToken(appid, accessToken_);
                return postRequest(postData_);
            }

            Logger.error("UnHandler Exception!");
            return false;
        }
        return super.handlerResponse(responseResult);
    }

    private String accessToken_;
}
