package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoBySubMerchantId() {
        long subMerchantId = Long.parseLong(getParameter("subMerchantId").toString());
        return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantId));
    }
}
