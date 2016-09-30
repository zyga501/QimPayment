package pf.merchant.action;

import pf.database.alipay.AliMerchantInfo;
import pf.database.bestpay.BtMerchantInfo;
import pf.database.jdpay.JdMerchantInfo;
import pf.database.merchant.OAuthLogin;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxSubMerchantAct;
import pf.database.weixin.WxSubMerchantInfo;
import framework.action.AjaxActionSupport;
import pf.weixin.api.OpenId;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantAction extends AjaxActionSupport {
    public String chkwx(){
        String id = getParameter("id").toString();
        String dt = getParameter("dt").toString();
        OAuthLogin oAuthLogin = OAuthLogin.getOAuthLoginByRmdno(dt);
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
                        WxSubMerchantAct submerchantact = new WxSubMerchantAct().getGoodstagById(submerchantinfo.getId());
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
        WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(jsonObject.getLong("mid"));
        if (subMerchantInfo != null) {
            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
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
        WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(Long.parseLong(mid));
        if (subMerchantInfo != null) {
            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
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
            WxSubMerchantInfo subMerchantInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (null == subMerchantInfo){
                resultMap.put("hasweixin", "0");
            }else {
                resultMap.put("hasweixin", "1");
            }
            JdMerchantInfo merchantInfo = JdMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (null == merchantInfo){
                resultMap.put("hasjdpay", "0");
            }else {
                resultMap.put("hasjdpay", "1");
            }
            AliMerchantInfo alimerchantInfo = AliMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (null == alimerchantInfo){
                resultMap.put("hasalipay", "0");
            }else {
                resultMap.put("hasalipay", "1");
            }
            SubMerchantInfo subMerchantInfo1 = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
                BtMerchantInfo btMerchantInfo = BtMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
                if (null == btMerchantInfo){
                resultMap.put("hasbestpay", "0");
            }else {
                resultMap.put("hasbestpay", "1");
            }

            resultMap.put("uid", String.valueOf(subMerchantUser.getId()));
            resultMap.put("storename",subMerchantUser.getStoreName());
            resultMap.put("uname",subMerchantUser.getUserName());
            SubMerchantInfo submerchantinfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            resultMap.put("businessname",submerchantinfo.getName());
            resultMap.put("ads",submerchantinfo.getAds());
            WxSubMerchantAct  submerchantact = new WxSubMerchantAct().getGoodstagById(submerchantinfo.getId());
            resultMap.put("goodstag",null==submerchantact?"":submerchantact.getGoodsTag());
            return AjaxActionComplete(true, resultMap);
        }

        return AjaxActionComplete(false);
    }

}
