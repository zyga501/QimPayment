package com.merchant.action;

import com.framework.ProjectSettings;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.ClassUtils;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.weixin.api.OpenId;
import com.database.weixin.MerchantInfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubMerchantAction extends AjaxActionSupport {
    public String regsiterSubMerchantInfo() {
        // insert base info
        String storeName = getParameter("storeName").toString();
        String address = getParameter("address").toString();
        long merchantId = Long.parseLong(getParameter("merchantId").toString());
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        long subMerchantId = new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId();
        subMerchantInfo.setId(subMerchantId);
        subMerchantInfo.setMerchantId(merchantId);
        subMerchantInfo.setName(storeName);
        subMerchantInfo.setAddress(address);
        return AjaxActionComplete(SubMerchantInfo.insertSubMerchantInfo(subMerchantInfo, () -> {
                // insert default user
                SubMerchantUser subMerchantUser = new SubMerchantUser();
                subMerchantUser.setId(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
                subMerchantUser.setSubMerchantId(subMerchantId);
                subMerchantUser.setUserName("001");
                subMerchantUser.setUserPwd("001");
                subMerchantUser.setStoreName(storeName);
                return SubMerchantUser.insertSubMerchantUserInfo(subMerchantUser, () -> {
                        // insert weixin info
                        String sub_mch_id = getParameter("sub_mch_id").toString();
                        com.database.weixin.SubMerchantInfo subMerchantWeixinInfo = new com.database.weixin.SubMerchantInfo();
                        subMerchantWeixinInfo.setId(subMerchantId);
                        subMerchantWeixinInfo.setSubId(sub_mch_id);
                        subMerchantWeixinInfo.setMerchantId(merchantId);
                        return com.database.weixin.SubMerchantInfo.insertSubMerchantInfo(subMerchantWeixinInfo);
                    }
                );
            }
        ));
    }

    public void preUpdateWeixinIdById() throws IOException {
        String subMerchantId = getParameter("id").toString();
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(Long.parseLong(subMerchantId));
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                String redirect_uri = getRequest().getRequestURL().toString();
                redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/'));
                redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/') + 1) + "merchant/bindSubMerchant.jsp";
                String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                        merchantInfo.getAppid(), redirect_uri, subMerchantId);
                getResponse().sendRedirect(perPayUri);
                return;
            }
        }

        Logger.error("UpdateWeixinIdById Error!");
    }

    public String updateWeixinIdById() throws Exception {
        String subMerchantId = getParameter("id").toString();
        String code = getParameter("code").toString();
        if (subMerchantId.isEmpty() || code.isEmpty()) {
            return AjaxActionComplete(false);
        }

        long id = Long.parseLong(subMerchantId);
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(id);
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                OpenId openId = new OpenId(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                if (openId.getRequest()) {
                    subMerchantInfo = new SubMerchantInfo();
                    subMerchantInfo.setId(id);
                    subMerchantInfo.setWeixinId(openId.getOpenId());
                    if (SubMerchantInfo.updateWeixinIdById(subMerchantInfo)) {
                        return AjaxActionComplete(true);
                    }
                }
            }
        }

        return AjaxActionComplete(false);
    }

    public String updateWeixinIdDirectById() {
        long id = Long.parseLong(getParameter("id").toString());
        String weixinId = getParameter("weixinId").toString();
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(id);
        subMerchantInfo.setWeixinId(weixinId);
        if (SubMerchantInfo.updateWeixinIdById(subMerchantInfo)) {
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }

    public String updateLogoById() throws IOException {
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
                return AjaxActionComplete(true);
            }
        }

        return AjaxActionComplete(false);
    }

    public String updateSubMerchantWeixinInfo() {
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(Long.parseLong(getParameter("id").toString()));
        subMerchantInfo.setTemplateId(getParameter("templateId").toString());
        if (SubMerchantInfo.updateWeixinInfoById(subMerchantInfo)) {
            com.database.weixin.SubMerchantInfo subMerchantWeixinInfo = new com.database.weixin.SubMerchantInfo();
            subMerchantWeixinInfo.setId(Long.parseLong(getParameter("id").toString()));
            subMerchantWeixinInfo.setAppid(getParameter("appid").toString());
            subMerchantWeixinInfo.setAppsecret(getParameter("appsecret").toString());
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }

    public String getSubMerchantInfo() {
        long subMerchantId = Long.parseLong(getParameter("id").toString());
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantId);
        Map<String, Object> resultMap = ClassUtils.convertToMap(subMerchantInfo);
        return AjaxActionComplete(resultMap);
    }

    public String getSubMerchantIdByCompatibleId() {
        String compatibleId = getParameter("compatibleId").toString();
        long subMerchantId = com.database.weixin.SubMerchantInfo.getSubMerchantIdByCompatibleId(compatibleId);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("subMerchantId", subMerchantId);
        return AjaxActionComplete(resultMap);
    }
}
