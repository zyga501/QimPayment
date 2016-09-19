package pf.merchant.action;

import framework.action.AjaxActionSupport;
import framework.utils.IdWorker;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxSubMerchantInfo;
import pf.weixin.api.OpenId;

import java.io.IOException;
import java.util.List;

public class SubMerchantUserAction extends AjaxActionSupport {
    public String getInfoByWeixinSubMerchantId() {
        String sub_mch_id = getParameter("sub_mch_id").toString();
        String merchantid = getParameter("merchantId").toString();
        WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoBySubId(sub_mch_id);
         if ((null!=subMerchantInfo) && (merchantid.equals(String.valueOf(subMerchantInfo.getMerchantId())))) {
                 return AjaxActionComplete(SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId()));
             }
         else
             return AjaxActionComplete(false);
    }

    public void preUpdateWeixinIdById() throws IOException {
        preUpdateWeixinId(getParameter("id").toString());
    }

    private void  preUpdateWeixinId(String subMerchantUserId) throws IOException {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    String redirect_uri = getRequest().getRequestURL().toString();
                   // redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/'));
                    redirect_uri = getRequest().getScheme()+"://"+getRequest().getServerName()+getRequest().getContextPath()+"/" + "merchant/bindSubMerchantUser.jsp";
                    String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                                    "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                           subMerchantInfo.getAppid()==null?merchantInfo.getAppid():subMerchantInfo.getAppid(), redirect_uri, subMerchantUserId);
                    System.out.print(perPayUri);
                    getResponse().sendRedirect(perPayUri);
                    return;
                }
            }
        }
        ProjectLogger.error("UpdateWeixinIdById Error!");
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
                WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    OpenId openId = new OpenId(subMerchantInfo.getAppid()==null?merchantInfo.getAppid():subMerchantInfo.getAppid(),
                           subMerchantInfo.getAppsecret()==null?merchantInfo.getAppsecret():subMerchantInfo.getAppsecret(), code);
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
        WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoBySubId(submchid);
        List<SubMerchantUser> subMerchantUserList =  SubMerchantUser.getSubMerchantUserBySubMerchantId(subMerchantInfo.getId());
        for (SubMerchantUser submerchantuser: subMerchantUserList) {
            if  (submerchantuser.getUserName().equals(ucode)){
                preUpdateWeixinId(String.valueOf(submerchantuser.getId()));
                return;
            }
        }
    }
}
