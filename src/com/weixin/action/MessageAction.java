package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.AccessToken;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;

import java.io.IOException;

public class MessageAction extends AjaxActionSupport {
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

    public String sendTemplateMessage() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    String sendMessageUrl = String.format(SEND_MESSAGE, accessToken);
                }
            }
        }
        return AjaxActionComplete();
    }

    private String buildTemplateMessage() {
        return "";
    }
}
