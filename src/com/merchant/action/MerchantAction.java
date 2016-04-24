package com.merchant.action;

import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.SubMerchantAct;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.Logger;
import com.weixin.api.OpenId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MerchantAction extends AjaxActionSupport {
    public String wx() throws Exception {
        String appid = "wx0bfa8f7ec59b1f33";
        String appsecret = "9386215269d6eb50c14199089890050f";
        Map<String, String> map = new HashMap<>();
        map.put("ucode","001");
        map.put("storename","分店名称");
        OpenId openId = new OpenId(appid, appsecret, getParameter("code").toString());
        if (openId.getRequest()) {
            map.put("openid",openId.getOpenId());
        }
        else {
            Logger.warn(this.getClass().getName() + " Get OpenId Failed!");
            return AjaxActionComplete();
        }
        return AjaxActionComplete(map);
    }

    public void oauthWX() throws IOException {
        String dt = getParameter("dt").toString();
        String mac = getParameter("mac").toString();
        String redirect_uri = "http://192.168.1.68:9090/weixin/rtopenid.jsp";// + "rtopenid.jsp";.getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") )+ 1
        String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                        "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                "appid", redirect_uri, "dt:"+dt+",mac:"+mac);
        getResponse().sendRedirect(redirect_uri);
    }

    public String Login() throws IOException {
        String uname = getParameter("un").toString();
        String upwd = getParameter("upwd").toString();
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        subMerchantUser.setSubMerchantId(Long.parseLong(getParameter("id").toString()));
        subMerchantUser.setUserName(getParameter("un").toString());
        subMerchantUser.setUserPwd(getParameter("upwd").toString());
        subMerchantUser = SubMerchantUser.getSubMerchantUserByAccount(subMerchantUser);
        Map<String, String> resultMap = new HashMap<>();
        if (null != subMerchantUser) {
            resultMap.put("uid", String.valueOf(subMerchantUser.getId()));
            resultMap.put("storename",subMerchantUser.getStoreName());
            resultMap.put("uname",subMerchantUser.getUserName());
            SubMerchantInfo submerchantinfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            resultMap.put("businessname",submerchantinfo.getName());
            resultMap.put("ads",submerchantinfo.getAds());
            SubMerchantAct  submerchantact = new SubMerchantAct().getGoodstagById(submerchantinfo.getId());
            resultMap.put("goodstag",null==submerchantact?"":submerchantact.getGoodsTag());
            return AjaxActionComplete(true, resultMap);
        }

        return AjaxActionComplete(false);
    }

    public void FetchLogo() throws IOException {
        super.getResponse().setHeader("Pragma", "No-cache");
        super.getResponse().setHeader("Cache-Control", "no-cache");
        super.getResponse().setDateHeader("Expires", 0);
        super.getResponse().setContentType("image/jpeg");
        try {
            byte[] logo = SubMerchantInfo.getSubMerchantLogoById(Long.parseLong(getParameter("id").toString()));
            if (logo != null) {
                super.getResponse().getOutputStream().write(logo);
            }
        }
        catch (Exception e){

        }
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
    }
}
