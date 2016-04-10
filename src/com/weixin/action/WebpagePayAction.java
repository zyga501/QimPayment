package com.weixin.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.merchant.database.IdMapUUID;
import com.merchant.database.SubMerchantUser;

public class WebpagePayAction extends AjaxActionSupport {
    private final static String SCANPAY = "scanPay";

    private String storeName;
    private String userName;
    private String subMerchantId;

    public String getStoreName() {
        return storeName;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

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
         storeName = subMerchantUser.getStoreName();
         userName = subMerchantUser.getUserName();
         subMerchantId = new Long(subMerchantUser.getSubMerchantId()).toString();
        return SCANPAY;
    }
}
