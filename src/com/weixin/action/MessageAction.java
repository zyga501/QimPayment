package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.AccessToken;
import com.weixin.api.Message;
import com.weixin.api.RequestData.MessageRequestData;
import com.weixin.api.UserInfo;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;

import java.io.IOException;

public class MessageAction extends AjaxActionSupport {
    public String sendTemplateMessage() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    MessageRequestData messageRequestData = new MessageRequestData();
                    messageRequestData.touser = subMerchantUser.getWeixinId();
                    messageRequestData.template_id = subMerchantInfo.getTemplateId();
                    messageRequestData.url = ""; // TODO
                    UserInfo userInfo = new UserInfo(merchantInfo.getAppid(), subMerchantUser.getWeixinId());
                    if (userInfo != null) {
                    }
                }
            }
        }
        return AjaxActionComplete();
    }

    private String buildTemplateMessage() {
        return "";
    }
}
