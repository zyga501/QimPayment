package com.merchant.action;

import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.framework.ProjectSettings;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.weixin.api.OpenId;

import java.io.IOException;
import java.util.List;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoByWeixinSubMerchantId() {
        String sub_mch_id = getParameter("sub_mch_id").toString();
        com.database.weixin.SubMerchantInfo subMerchantInfo = com.database.weixin.SubMerchantInfo.getSubMerchantInfoBySubId(sub_mch_id);
        return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId()));
    }

    public void preUpdateWeixinIdById() throws IOException {
        preUpdateWeixinId(getParameter("id").toString());
    }

    private void  preUpdateWeixinId(String subMerchantUserId) throws IOException {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String redirect_uri = getRequest().getRequestURL().toString();
                    redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/'));
                    redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/') + 1) + "merchant/bindSubMerchantUser.jsp";
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
                        subMerchantUser = new SubMerchantUser();
                        subMerchantUser.setId(id);
                        subMerchantUser.setWeixinId(openId.getOpenId());
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
        subMerchantUser.setId(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        subMerchantUser.setSubMerchantId(Long.parseLong(getParameter("subMerchantId").toString()));
        subMerchantUser.setUserName(getParameter("userName").toString());
        subMerchantUser.setUserPwd(getParameter("userPwd").toString());
        subMerchantUser.setStoreName(getParameter("storeName").toString());
        return AjaxActionComplete(SubMerchantUser.insertSubMerchantUserInfo(subMerchantUser, null));
    }
    public void oldBindMsg() throws IOException {
        if ((null == getParameter("method"))|| (! getParameter("method").toString().equals("bindmsg"))){
            return;
        }
        if (null == getParameter("bingencode")){
            return;
        }
        String bingencode = getParameter("bingencode").toString();
        String submchid = bingencode.split("_")[0];
        String ucode = bingencode.split("_")[1];
        com.database.weixin.SubMerchantInfo subMerchantInfo = com.database.weixin.SubMerchantInfo.getSubMerchantInfoBySubId(submchid);
        List<SubMerchantUser> subMerchantUserList =  SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId());
        for (SubMerchantUser submerchantuser: subMerchantUserList) {
            if  (submerchantuser.getUserName().equals(ucode)){
                preUpdateWeixinId(String.valueOf(submerchantuser.getId()));
                return;
            }
        }

    }
}
