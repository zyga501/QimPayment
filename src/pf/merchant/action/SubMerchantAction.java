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

import java.io.DataInputStream;
import java.io.FileInputStream;
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
        WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(merchantId);
        subMerchantInfo.setTemplateId(merchantInfo.getTemplateId());
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
                        WxSubMerchantInfo subMerchantWeixinInfo = new WxSubMerchantInfo();
                        subMerchantWeixinInfo.setId(subMerchantId);
                        subMerchantWeixinInfo.setSubId(sub_mch_id);
                        subMerchantWeixinInfo.setMerchantId(merchantId);
                        return WxSubMerchantInfo.insertSubMerchantInfo(subMerchantWeixinInfo);
                    }
                );
            }
        ));
    }

    public void preUpdateWeixinIdById() throws IOException {
        String subMerchantId = getParameter("id").toString();
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(Long.parseLong(subMerchantId));
        if (subMerchantInfo != null) {
            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                String redirect_uri = getRequest().getRequestURL().toString();
                redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/'));
                redirect_uri = redirect_uri.substring(0, redirect_uri.lastIndexOf('/') + 1) + "merchant/bindSubMerchant.jsp";
                String perPayUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                                "%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                        subMerchantInfo.getAppid()==null?merchantInfo.getAppid():subMerchantInfo.getAppid(), redirect_uri, subMerchantId);
                getResponse().sendRedirect(perPayUri);
                return;
            }
        }

        ProjectLogger.error("UpdateWeixinIdById Error!");
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
            WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo != null) {
                OpenId openId = new OpenId(subMerchantInfo.getAppid()==null?merchantInfo.getAppid():subMerchantInfo.getAppid(),
                       subMerchantInfo.getAppsecret()==null?merchantInfo.getAppsecret():subMerchantInfo.getAppsecret(), code);
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
            WxSubMerchantInfo subMerchantWeixinInfo = new WxSubMerchantInfo();
            subMerchantWeixinInfo.setId(Long.parseLong(getParameter("id").toString()));
            subMerchantWeixinInfo.setAppid(getParameter("appid").toString());
            subMerchantWeixinInfo.setAppsecret(getParameter("appsecret").toString());
            return AjaxActionComplete(WxSubMerchantInfo.updateWeixinInfoById(subMerchantWeixinInfo));
        }

        return AjaxActionComplete(false);
    }


    public String updateSubMerchantAds() {
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(Long.parseLong(getParameter("id").toString()));
        subMerchantInfo.setAds(getParameter("ads").toString());
        return AjaxActionComplete(SubMerchantInfo.updateAdsById(subMerchantInfo));
    }

    public String getSubMerchantInfo() {
        long subMerchantId = Long.parseLong(getParameter("id").toString());
        SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantId);
        Map<String, Object> resultMap = new HashMap<>();//ClassUtils.convertToMap(subMerchantInfo);
        resultMap.put("ads",subMerchantInfo!=null?subMerchantInfo.getAds():"");
        return AjaxActionComplete(resultMap);
    }

    public String getSubMerchantIdByCompatibleId() {
        String compatibleId = getParameter("compatibleId").toString();
        long subMerchantId = WxSubMerchantInfo.getSubMerchantIdByCompatibleId(compatibleId);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("subMerchantId", subMerchantId);
        return AjaxActionComplete(resultMap);
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
                try {
                    getResponse().getOutputStream().write(buffer, 0, readSize);
                    readSize = fileInputStream.read(buffer);
                }
                catch (Exception ee){
                    break;
                }
            }
            fileInputStream.close();
        }
        super.getResponse().getOutputStream().flush();
        super.getResponse().getOutputStream().close();
    }
}
