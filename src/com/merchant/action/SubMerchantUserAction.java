package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;

import java.util.HashMap;
import java.util.Map;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoByWeixinSubMerchantId() {
        String sub_mch_id = getParameter("sub_mch_id").toString();
        com.weixin.database.SubMerchantInfo subMerchantInfo = com.weixin.database.SubMerchantInfo.getSubMerchantInfoBySubId(sub_mch_id);
        return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId()));
    }

    public String updateWeixinIdByUserId() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("resultCode", "Failed");

        long id = Long.parseLong(getParameter("id").toString());
        String weixinId = getParameter("weixinId").toString();
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        subMerchantUser.setId(id);
        subMerchantUser.setWeixinId(weixinId);
        if (SubMerchantUser.updateWeixinIdByUserId(subMerchantUser)) {
            resultMap.put("resultCode", "Succeed");
        }

        return AjaxActionComplete(resultMap);
    }
}
