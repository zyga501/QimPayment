package com.alipay.action;

import com.database.merchant.IdMapUUID;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import com.framework.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class WebpagePayAction extends AjaxActionSupport {

    private String storeName;
    private String userName;
    private String subMerchantId;
    private ByteArrayInputStream subMerchantLogo;

    public String getStoreName() {
        return storeName;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

    public ByteArrayInputStream getSubMerchantLogo() {
        return subMerchantLogo;
    }

    public void prePay() throws IOException, IOException {
        String appid = new String();
        String subMerchantUserId = new String();
        if (!StringUtils.convertNullableString(getParameter("id")).isEmpty()) {
            subMerchantUserId = getParameter("id").toString();
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser != null) {
                SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
                if (subMerchantInfo != null) {
                    MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        getRequest().getSession().setAttribute("storename",subMerchantUser.getStoreName());
                        getRequest().getSession().setAttribute("ucode",subMerchantUser.getUserName());
                        getRequest().getSession().setAttribute("subMerchantId",subMerchantInfo.getId());
                        getRequest().getSession().setAttribute("id", subMerchantUser.getId());
                        appid = merchantInfo.getAppid();
                    }
                }
            }
        }
        else {
            IdMapUUID idMapUUID = IdMapUUID.getMappingByUUID(getParameter("odod").toString());
            if (idMapUUID != null) {
                subMerchantUserId = String.valueOf(idMapUUID.getId());
                SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(idMapUUID.getId());
                getRequest().getSession().setAttribute("storename", subMerchantUser.getStoreName());
                getRequest().getSession().setAttribute("ucode", subMerchantUser.getUserName());
                SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                getRequest().getSession().setAttribute("id", subMerchantUser.getId());
                getRequest().getSession().setAttribute("subMerchantId", subMerchantInfo.getId());
                appid = merchantInfo.getAppid();
            }
        }
        if (appid.isEmpty()) {
            Logger.warn("prePay Failed!");
            return;
        }

        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "alipay/jspay.jsp";
        getResponse().sendRedirect(redirect_uri);
    }

}
