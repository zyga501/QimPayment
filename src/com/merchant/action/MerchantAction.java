package com.merchant.action;

import com.database.merchant.OAuthLogin;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.database.weixin.MerchantInfo;
import com.database.weixin.SubMerchantAct;
import com.framework.action.AjaxActionSupport;
import com.weixin.api.OpenId;
import net.sf.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantAction extends AjaxActionSupport {
    public String chkwx(){
        String id = getParameter("id").toString();
        String dt = getParameter("dt").toString();
        OAuthLogin  oAuthLogin = OAuthLogin.getOAuthLoginByRmdno(dt);
        if (null!=oAuthLogin){
            if ((new Date()).getTime() - oAuthLogin.getInserttime().getTime()<15000){
                List<SubMerchantUser> subMerchantUserList = SubMerchantUser.getSubMerchantUserBySubMerchantId(Long.parseLong(id));
                for (SubMerchantUser submerchantuser: subMerchantUserList) {
                    if  (submerchantuser.getWeixinId().equals(oAuthLogin.getOpenid())){
                        Map<String, String> resultMap = new HashMap<>();
                        resultMap.put("uid", String.valueOf(submerchantuser.getId()));
                        resultMap.put("storename",submerchantuser.getStoreName());
                        resultMap.put("uname",submerchantuser.getUserName());
                        SubMerchantInfo submerchantinfo = SubMerchantInfo.getSubMerchantInfoById(submerchantuser.getSubMerchantId());
                        resultMap.put("businessname",submerchantinfo.getName());
                        resultMap.put("ads",submerchantinfo.getAds());
                        SubMerchantAct  submerchantact = new SubMerchantAct().getGoodstagById(submerchantinfo.getId());
                        resultMap.put("goodstag",null==submerchantact?"":submerchantact.getGoodsTag());
                        return AjaxActionComplete(true, resultMap);
                    }
                }
            }
        }
        return AjaxActionComplete(false);
    }

    public String wx() throws Exception {
        String appid = "";
        String appsecret = "";
        JSONObject jsonObject = JSONObject.fromObject( getRequest().getSession().getAttribute("datajson"));
        com.database.weixin.SubMerchantInfo subMerchantInfo = com.database.weixin.SubMerchantInfo.getSubMerchantInfoById(jsonObject.getLong("mid"));
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                appid =  merchantInfo.getAppid();
                appsecret =  merchantInfo.getAppsecret();
                OpenId openId = new OpenId(appid, appsecret, getParameter("code").toString());
                if (openId.getRequest()) {
                    List<SubMerchantUser> subMerchantUserList = SubMerchantUser.getSubMerchantUserBySubMerchantId(jsonObject.getLong("mid"));
                    for (SubMerchantUser submerchantuser: subMerchantUserList) {
                        if  (submerchantuser.getWeixinId().equals(openId.getOpenId())){
                            OAuthLogin  oAuthLogin = new OAuthLogin();
                            oAuthLogin.setOpenid(openId.getOpenId());
                            oAuthLogin.setInserttime(new Date());
                            oAuthLogin.setRmdno(jsonObject.getString("dt"));
                            OAuthLogin.insertOAuthLogin(oAuthLogin);
                            return AjaxActionComplete(true);
                        }
                    }
                }
            }
        }
        return AjaxActionComplete(false);
    }

    public void oauthWX() throws IOException {
        String appid = "";//"wx0bfa8f7ec59b1f33";
        String dt = getParameter("dt").toString();
        String mid = getParameter("id").toString();
        System.out.println("mid="+mid);
        com.database.weixin.SubMerchantInfo subMerchantInfo = com.database.weixin.SubMerchantInfo.getSubMerchantInfoById(Long.parseLong(mid));
        if (subMerchantInfo != null) {
            MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                appid = merchantInfo.getAppid();
            }
        }
        getRequest().getSession().setAttribute("datajson", "{'dt':'"+dt+"','mid':'"+mid+"'}");
        String redirect_uri =  getRequest().getScheme()+"://" + getRequest().getServerName() + getRequest().getContextPath() + "/weixin/rtopenid.jsp";
        String petOpenidUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                        "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect",
                appid, redirect_uri);
        getResponse().sendRedirect(petOpenidUri);
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
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(getRequest()
                    .getServletContext().getRealPath("/")
                    + "image/defaultlogo.jpg");
            int readSize = fileInputStream.read(buffer);
            while (readSize != -1) {
                getResponse().getOutputStream().write(buffer, 0, readSize);
                readSize = fileInputStream.read(buffer);
            }
            fileInputStream.close();
        }
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
    }
}
