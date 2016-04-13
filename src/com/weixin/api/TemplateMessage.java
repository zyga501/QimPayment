package com.weixin.api;

import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.RequestData.TemplateMessageRequestData;
import com.weixin.database.MerchantInfo;
import com.weixin.database.OrderInfo;

import java.util.LinkedHashMap;

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
        return super.handlerResponse(responseResult);
    }

    private String accessToken_;
}
