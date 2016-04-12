package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;
import com.weixin.utils.OAuth2;

import java.io.IOException;

public class MessageAction extends AjaxActionSupport {
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

    public String sendTemplateMessage() throws IOException {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String accessToken = new String();
                    if (merchantInfo.getAccessToken().isEmpty()) {
                        accessToken = OAuth2.fetchAccessToken(merchantInfo.getAppid(), merchantInfo.getAppsecret());
                        MerchantInfo.updateAccessToken(merchantInfo.getId(), accessToken);
                    }
                    else {
                        accessToken = merchantInfo.getAccessToken();
                    }
                    String sendMessage = String.format(SEND_MESSAGE, accessToken);
                }
            }
        }
        return AjaxActionComplete();
    }
}
