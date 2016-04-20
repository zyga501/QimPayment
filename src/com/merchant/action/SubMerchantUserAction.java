package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.merchant.database.SubMerchantInfo;
import com.merchant.database.SubMerchantUser;
import com.weixin.api.OpenId;
import com.weixin.database.MerchantInfo;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoByWeixinSubMerchantId() {
        String sub_mch_id = getParameter("sub_mch_id").toString();
        com.weixin.database.SubMerchantInfo subMerchantInfo = com.weixin.database.SubMerchantInfo.getSubMerchantInfoBySubId(sub_mch_id);
        return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId()));
    }

    public void preUpdateWeixinIdById() throws IOException {
        String subMerchantUserId = getParameter("id").toString();
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "merchant/bindSubMerchantUser.jsp";
                    String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                                    "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                            merchantInfo.getAppid(), redirect_uri, subMerchantUserId);
                    getResponse().sendRedirect(perPayUri);
                }
            }
        }

        Logger.error("UpdateWeixinIdById Error!");
    }

    public String updateWeixinIdById() throws Exception {
        String subMerchantUserId = getParameter("id").toString();
        String code = getParameter("code").toString();
        if (subMerchantUserId.isEmpty() || code.isEmpty()) {
            return AjaxActionComplete(false);
        }

        long id = Long.parseLong(subMerchantUserId);
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(id);
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    OpenId openId = new OpenId(merchantInfo.getAppid(), merchantInfo.getAppsecret(), code);
                    if (openId.getRequest()) {
                        String weixinId = code;
                        subMerchantUser = new SubMerchantUser();
                        subMerchantUser.setId(id);
                        subMerchantUser.setWeixinId(weixinId);
                        if (SubMerchantUser.updateWeixinIdById(subMerchantUser)) {
                            return AjaxActionComplete(true);
                        }
                    }
                }
            }
        }

        return AjaxActionComplete(false);
    }

    public String updateWeixinIdDirectById() {
        long id = Long.parseLong(getParameter("id").toString());
        String weixinId = getParameter("weixinId").toString();
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        subMerchantUser.setId(id);
        subMerchantUser.setWeixinId(weixinId);
        if (SubMerchantUser.updateWeixinIdById(subMerchantUser)) {
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }

    public String updateStoreNameById() {
        long id = Long.parseLong(getParameter("id").toString());
        String storeName = getParameter("storeName").toString();
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        subMerchantUser.setId(id);
        subMerchantUser.setStoreName(storeName);
        if (SubMerchantUser.updateStoreNameById(subMerchantUser)) {
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }

    public String registerSubMerchantUserInfo() {
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        subMerchantUser.setId(new IdWorker(0).nextId());
        subMerchantUser.setSubMerchantId(Long.parseLong(getParameter("subMerchantId").toString()));
        subMerchantUser.setUserName(getParameter("userName").toString());
        subMerchantUser.setUserPwd(getParameter("userPwd").toString());
        subMerchantUser.setStoreName(getParameter("storeName").toString());
        SqlSession sqlsubMerchantUserSession = SubMerchantUser.insertSubMerchantUserInfo(subMerchantUser);
        if (sqlsubMerchantUserSession != null) {
            sqlsubMerchantUserSession.commit();
            sqlsubMerchantUserSession.close();
            return AjaxActionComplete(true);
        }

        return AjaxActionComplete(false);
    }
}
