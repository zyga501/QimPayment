package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.IdWorker;
import com.merchant.database.SubMerchantInfo;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class SubMerchantAction extends AjaxActionSupport {
    public String insertSubMerchantBaseInfo() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("resultCode", "Failed");
        // insert base info
        String storeName = getParameter("storeName").toString();
        String address = getParameter("address").toString();
        long merchantId = Long.parseLong(getParameter("merchantId").toString());
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(new IdWorker(0).nextId());
        subMerchantInfo.setMerchantId(merchantId);
        subMerchantInfo.setName(storeName);
        subMerchantInfo.setAddress(address);
        SqlSession sqlSession = SubMerchantInfo.insertSubMerchantBaseInfo(subMerchantInfo);
        if (sqlSession != null) {
            // insert weixin info
            String sub_mch_id = getParameter("sub_mch_id").toString();
            com.weixin.database.SubMerchantInfo subMerchantWeixinInfo = new com.weixin.database.SubMerchantInfo();
            subMerchantWeixinInfo.setId(new IdWorker(0).nextId());
            subMerchantWeixinInfo.setSubId(sub_mch_id);
            subMerchantWeixinInfo.setMerchantId(merchantId);
            if (com.weixin.database.SubMerchantInfo.insertSubMerchantInfo(subMerchantWeixinInfo)) {
                sqlSession.commit();
                sqlSession.close();
                resultMap.put("resultCode", "Succeed");
            }
        }
        return AjaxActionComplete(resultMap);
    }
}
