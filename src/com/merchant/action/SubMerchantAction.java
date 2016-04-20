package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.OpenId;
import com.weixin.database.MerchantInfo;
import org.apache.ibatis.session.SqlSession;

import java.io.DataInputStream;
import java.io.IOException;

public class SubMerchantAction extends AjaxActionSupport {
    public String regsiterSubMerchantInfo() {
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
                    return AjaxActionComplete(true);
                }
            }
        }

        return AjaxActionComplete(false);
    }

    public void preUpdateWeixinIdById() throws IOException {
        String subMerchantId = getParameter("id").toString();
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(Long.parseLong(subMerchantId));
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "merchant/bindSubMerchant.jsp";
                String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                        merchantInfo.getAppid(), redirect_uri, subMerchantId);
                getResponse().sendRedirect(perPayUri);
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
                    String weixinId = code;
                    subMerchantInfo = new SubMerchantInfo();
                    subMerchantInfo.setId(id);
                    subMerchantInfo.setWeixinId(weixinId);
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
        subMerchantInfo.setAppid(getParameter("appid").toString());
        subMerchantInfo.setAppsecret(getParameter("appsecret").toString());
        subMerchantInfo.setTemplateId(getParameter("templateId").toString());
        if (SubMerchantInfo.updateWeixinInfoById(subMerchantInfo)) {
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }
}
