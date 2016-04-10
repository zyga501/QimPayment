package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.merchant.database.IdMapUUID;
import com.merchant.database.SubMerchantUser;
import com.weixin.database.MerchantInfo;
import com.weixin.database.SubMerchantInfo;

public class WebpagePayAction extends AjaxActionSupport {
    private final static String SCANPAY = "scanPay";

    private String storeName;
    private String userName;
     public String scanPay(){
         String subMerchantUserId = new String();
         if (!StringUtils.convertNullableString(getParameter("id")).isEmpty()) {
             subMerchantUserId = getParameter("id").toString();
         }
         else { // compatible old api
             IdMapUUID idMapUUID = IdMapUUID.getMappingByUUID( getParameter("odod").toString());
             if (idMapUUID != null){
                 subMerchantUserId = String.valueOf(idMapUUID.getId());
             }
         }

         SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
         getRequest().getSession().setAttribute("storename",subMerchantUser.getStoreName());
         getRequest().getSession().setAttribute("ucode",subMerchantUser.getUserName());
         SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
         MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
        return SCANPAY;
    }
}
