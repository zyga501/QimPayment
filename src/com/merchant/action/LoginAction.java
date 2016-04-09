package com.merchant.action;

import com.framework.action.AjaxActionSupport;
import com.merchant.database.SubMerchantUser;
import com.weixin.utils.OAuth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginAction extends AjaxActionSupport {
    public String wx() throws IOException {
        String appid = "wx0bfa8f7ec59b1f33";
        String appsecret = "9386215269d6eb50c14199089890050f";
        String openid = OAuth2.fetchOpenid(appid,appsecret, getParameter("code").toString());
        Map<String, String> map = new HashMap<>();
        map.put("ucode","001");
        map.put("storename","分店名称");
        map.put("openid",openid);
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
        SubMerchantUser submerchantuser = SubMerchantUser.getSubMerchantUserByLogin(uname,upwd);
        Map<String, String> map = new HashMap<>();
        if (null != submerchantuser) {
            map.put("return","success");
            map.put("uid", String.valueOf(submerchantuser.getId()));
            map.put("storename",submerchantuser.getStoreName());
        }
        else {
            map.put("return","failure");
        }
        return AjaxActionComplete(map);
    }
}
