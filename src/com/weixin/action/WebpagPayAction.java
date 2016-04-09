package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.IdMapUUID;
import com.merchant.database.SubMerchantUser;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;

public class WebpagPayAction extends AjaxActionSupport {
    private final static String OAUTHPAY = "oauthpay";
    private final static String SCANPAY = "scanpay";
     public String scanpay(){
         IdMapUUID idMapUUID = IdMapUUID.getMappingByUUID( getParameter("odod").toString());
         if (idMapUUID != null){
             String subMerchantUserId = String.valueOf(idMapUUID.getId());
             SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(idMapUUID.getId());
             getRequest().getSession().setAttribute("storename",subMerchantUser.getStoreName());
             getRequest().getSession().setAttribute("ucode",subMerchantUser.getUserName());
             SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
             MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
         }
        return SCANPAY;
    }
}
