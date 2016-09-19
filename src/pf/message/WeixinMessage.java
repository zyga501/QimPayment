package pf.message;

import pf.database.alipay.AliOrderInfo;
import pf.database.jdpay.JdOrderInfo;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import pf.database.weixin.WxMerchantInfo;
import pf.database.weixin.WxOrderInfo;
import pf.database.weixin.WxSubMerchantInfo;
import framework.utils.StringUtils;
import pf.weixin.api.AccessToken;
import pf.weixin.api.RequestBean.TemplateMessageRequestData;
import pf.weixin.api.TemplateMessage;
import pf.weixin.api.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class WeixinMessage {
    public static boolean sendTemplateMessage(String transactionId) throws Exception {
        WxOrderInfo orderInfo = WxOrderInfo.getOrderInfoByTransactionId(transactionId);
        if (orderInfo == null) {
            return false;
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(orderInfo.getCreateUser());
        if ((subMerchantUser != null)&&(!subMerchantUser.getUserName().equals("999"))) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            String accessToken = new String();
            if (subMerchantInfo != null) {
                WxSubMerchantInfo subMerchantWeixinInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantInfo.getId());
                if (!(StringUtils.convertNullableString(subMerchantWeixinInfo.getAppid()).isEmpty()
                        && StringUtils.convertNullableString(subMerchantWeixinInfo.getAppsecret()).isEmpty())) {
                    accessToken = AccessToken.getAccessToken(subMerchantWeixinInfo.getAppid());
                }
                else {
                    WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    }
                }
            }

            if (!accessToken.isEmpty()) {
                TemplateMessageRequestData templateMessageRequestData = new TemplateMessageRequestData();
                templateMessageRequestData.template_id = subMerchantInfo.getTemplateId();
                UserInfo userInfo = new UserInfo(accessToken, orderInfo.getOpenId());
                templateMessageRequestData.nickName = "尊敬的商户，您成功收到"+(userInfo.getRequest()?userInfo.getNickname():"*".concat(orderInfo.getOpenId().substring(20,24)))+"的消费付款：";
                templateMessageRequestData.timeEnd = orderInfo.getTimeEnd();
                templateMessageRequestData.totalFee = orderInfo.getTotalFee() / 100.0;
                templateMessageRequestData.storeName = subMerchantUser.getStoreName();
                templateMessageRequestData.paytype = "微信";
                templateMessageRequestData.orderno = orderInfo.getOutTradeNo();
                templateMessageRequestData.remark = ("收银员："+subMerchantUser.getUserName()+"\\n"+subMerchantInfo.getAds()).replaceAll("\r\n","\\\\n");

                TemplateMessage templateMessage = new TemplateMessage(accessToken);
                if (!StringUtils.convertNullableString(subMerchantUser.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                if (!StringUtils.convertNullableString(subMerchantInfo.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantInfo.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                return true;
            }
        }

        return false;
    }
    public static boolean sendJDTemplateMessage(String transactionId) throws Exception {
        Map orderInfo = new HashMap<>();
        orderInfo = JdOrderInfo.getOrderInfoBytermno(transactionId);
        if (orderInfo == null) {
            return false;
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(orderInfo.get("term_no").toString()));
        if ((subMerchantUser != null)&&(!subMerchantUser.getUserName().equals("999"))) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            String accessToken = new String();
            if (subMerchantInfo != null) {
                WxSubMerchantInfo subMerchantWeixinInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantInfo.getId());
                if (!StringUtils.convertNullableString(subMerchantWeixinInfo.getAppid()).isEmpty()
                        && StringUtils.convertNullableString(subMerchantWeixinInfo.getAppsecret()).isEmpty()) {
                    accessToken = AccessToken.getAccessToken(subMerchantWeixinInfo.getAppid());
                }
                else {
                    WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    }
                }
            }
            if (!accessToken.isEmpty()) {
                TemplateMessageRequestData templateMessageRequestData = new TemplateMessageRequestData();
                templateMessageRequestData.template_id = subMerchantInfo.getTemplateId();
                templateMessageRequestData.nickName = "尊敬的 京东 商户，您成功收到"+orderInfo.get("user")+"消费付款：";
                templateMessageRequestData.timeEnd = orderInfo.get("pay_time").toString();
                templateMessageRequestData.totalFee = Double.parseDouble(orderInfo.get("amount").toString());
                templateMessageRequestData.storeName = subMerchantUser.getStoreName();
                templateMessageRequestData.paytype = "京东钱包";
                templateMessageRequestData.orderno = orderInfo.get("order_no").toString();
                templateMessageRequestData.remark = ("收银员："+subMerchantUser.getUserName()+"\\n"+subMerchantInfo.getAds()).replaceAll("\r\n","\\\\n");

                TemplateMessage templateMessage = new TemplateMessage(accessToken);
                if (!StringUtils.convertNullableString(subMerchantUser.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                if (!StringUtils.convertNullableString(subMerchantInfo.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantInfo.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                return true;
            }
        }

        return false;
    }
    public static boolean sendAliTemplateMessage(String out_trade_no) throws Exception {
        AliOrderInfo orderInfo ;
        orderInfo = AliOrderInfo.getOrderInfoByOrderNo(out_trade_no);//OrderInfo.getOrderInfoBytermno(transactionId);
        if (orderInfo == null) {
            return false;
        }

        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById((orderInfo.getCreateUser()));
        if ((subMerchantUser != null)&&(!subMerchantUser.getUserName().equals("999"))) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            String accessToken = new String();
            if (subMerchantInfo != null) {
                WxSubMerchantInfo subMerchantWeixinInfo = WxSubMerchantInfo.getSubMerchantInfoById(subMerchantInfo.getId());
                if (!StringUtils.convertNullableString(subMerchantWeixinInfo.getAppid()).isEmpty()
                        && StringUtils.convertNullableString(subMerchantWeixinInfo.getAppsecret()).isEmpty()) {
                    accessToken = AccessToken.getAccessToken(subMerchantWeixinInfo.getAppid());
                }
                else {
                    WxMerchantInfo merchantInfo = WxMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                    if (merchantInfo != null) {
                        accessToken = AccessToken.getAccessToken(merchantInfo.getAppid());
                    }
                }
            }
            if (!accessToken.isEmpty()) {
                TemplateMessageRequestData templateMessageRequestData = new TemplateMessageRequestData();
                templateMessageRequestData.template_id = subMerchantInfo.getTemplateId();
                int len =orderInfo.getOpenId().length();
                templateMessageRequestData.nickName = "尊敬的商户，您成功收到*".concat(orderInfo.getOpenId().substring(len-4,len))+"的消费付款：";
                templateMessageRequestData.timeEnd = orderInfo.getGmtPayment();
                templateMessageRequestData.totalFee = (orderInfo.getTotalAmount());
                templateMessageRequestData.storeName = subMerchantUser.getStoreName();
                templateMessageRequestData.paytype = "支付宝";
                templateMessageRequestData.orderno = orderInfo.getOutTradeNo().toString();
                templateMessageRequestData.remark = ("收银员："+subMerchantUser.getUserName()+"\\n"+subMerchantInfo.getAds()).replaceAll("\r\n","\\\\n");

                TemplateMessage templateMessage = new TemplateMessage(accessToken);
                if (!StringUtils.convertNullableString(subMerchantUser.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantUser.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                if (!StringUtils.convertNullableString(subMerchantInfo.getWeixinId()).trim().isEmpty()) {
                    templateMessageRequestData.touser = subMerchantInfo.getWeixinId();
                    templateMessage.postRequest(templateMessageRequestData.buildRequestData());
                }
                return true;
            }
        }

        return false;
    }
}