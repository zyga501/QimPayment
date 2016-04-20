package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.IdWorker;
import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import org.apache.ibatis.session.SqlSession;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubMerchantAction extends AjaxActionSupport {
    public String regsiterSubMerchantInfo() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("resultCode", "Failed");
        // insert base info
        String storeName = getParameter("storeName").toString();
        String address = getParameter("address").toString();
        long merchantId = Long.parseLong(getParameter("merchantId").toString());
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        long subMerchantId = new IdWorker(0).nextId();
        subMerchantInfo.setId(subMerchantId);
        subMerchantInfo.setMerchantId(merchantId);
        subMerchantInfo.setName(storeName);
        subMerchantInfo.setAddress(address);
        SqlSession sqlSubMerchantSession = SubMerchantInfo.insertSubMerchantInfo(subMerchantInfo);
        if (sqlSubMerchantSession != null) {
            // insert default user
            SubMerchantUser subMerchantUser = new SubMerchantUser();
            subMerchantUser.setId(new IdWorker(0).nextId());
            subMerchantUser.setSubMerchantId(subMerchantId);
            subMerchantUser.setUserName("001");
            subMerchantUser.setUserPwd("001");
            subMerchantUser.setStoreName(storeName);
            SqlSession sqlsubMerchantUserSession = SubMerchantUser.insertSubMerchantUserInfo(subMerchantUser);
            if (sqlsubMerchantUserSession != null) {
                // insert weixin info
                String sub_mch_id = getParameter("sub_mch_id").toString();
                com.weixin.database.SubMerchantInfo subMerchantWeixinInfo = new com.weixin.database.SubMerchantInfo();
                subMerchantWeixinInfo.setId(subMerchantId);
                subMerchantWeixinInfo.setSubId(sub_mch_id);
                subMerchantWeixinInfo.setMerchantId(merchantId);
                if (com.weixin.database.SubMerchantInfo.insertSubMerchantInfo(subMerchantWeixinInfo)) {
                    sqlSubMerchantSession.commit();
                    sqlSubMerchantSession.close();
                    sqlsubMerchantUserSession.commit();
                    sqlsubMerchantUserSession.close();
                    resultMap.put("resultCode", "Succeed");
                }
            }
        }
        return AjaxActionComplete(resultMap);
    }

    public String updateWeixinIdById() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("resultCode", "Failed");

        long id = Long.parseLong(getParameter("id").toString());
        String weixinId = getParameter("weixinId").toString();
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(id);
        subMerchantInfo.setWeixinId(weixinId);
        if (SubMerchantInfo.updateWeixinIdById(subMerchantInfo)) {
            resultMap.put("resultCode", "Succeed");
        }

        return AjaxActionComplete(resultMap);
    }

    public String updateLogoById() throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("resultCode", "Failed");

        long id = Long.parseLong(getParameter("id").toString());
        int contentLength = getRequest().getContentLength();
        if (contentLength > 0) {
            byte[] logoBuffer = new byte[contentLength];
            DataInputStream dataInputStream = new DataInputStream(getRequest().getInputStream());
            dataInputStream.readFully(logoBuffer);
            dataInputStream.close();
            SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
            subMerchantInfo.setId(id);
            subMerchantInfo.setLogo(logoBuffer);
            if (SubMerchantInfo.updateLogoById(subMerchantInfo)) {
                resultMap.put("resultCode", "Succeed");
            }
        }

        return AjaxActionComplete(resultMap);
    }
}
