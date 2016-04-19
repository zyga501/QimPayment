package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoByWeixinSubMerchantId() {
        String sub_mch_id = getParameter("sub_mch_id").toString();
        com.weixin.database.SubMerchantInfo subMerchantInfo = com.weixin.database.SubMerchantInfo.getSubMerchantInfoBySubId(sub_mch_id);
        return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId()));
    }
}
