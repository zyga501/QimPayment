package pf.bestpay.action;

import pf.database.merchant.IdMapUUID;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class WebpagePayAction extends AjaxActionSupport {
    private final static String SCANPAY = "scanPay";

    private String storeName;
    private String userName;
    private String subMerchantId;
    private ByteArrayInputStream subMerchantLogo;

    public String getStoreName() {
        return storeName;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

    public ByteArrayInputStream getSubMerchantLogo() {
        return subMerchantLogo;
    }

    public void prePay() throws IOException, IOException {
        String appid = new String();
        String subMerchantUserId = new String();
        if (!StringUtils.convertNullableString(getParameter("id")).isEmpty()) {
            subMerchantUserId = getParameter("id").toString();
        }
        else {
            IdMapUUID idMapUUID = IdMapUUID.getMappingByUUID(getParameter("odod").toString());
            if (idMapUUID != null) {
                subMerchantUserId =String.valueOf (idMapUUID.getId());
            }
        }
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(subMerchantUserId));
            if (subMerchantUser != null) {
                SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
                if (subMerchantInfo != null) {
                        getRequest().getSession().setAttribute("storename",subMerchantUser.getStoreName());
                        getRequest().getSession().setAttribute("ucode",subMerchantUser.getUserName());
                        getRequest().getSession().setAttribute("subMerchantId",subMerchantInfo.getId());
                        getRequest().getSession().setAttribute("id", subMerchantUser.getId());
                }
            }

        String redirect_uri = getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "bestpay/jspay.jsp";
        getResponse().sendRedirect(redirect_uri);
    }

}
