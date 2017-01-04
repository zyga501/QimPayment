package pf.alipay.action;

import pf.alipay.api.RequestBean.TradePayRequestData;
import pf.alipay.api.RequestBean.TradePreCreateRequestData;
import pf.alipay.api.TradePay;
import pf.alipay.api.TradePreCreate;
import pf.database.alipay.AliMerchantInfo;
import pf.database.alipay.AliOrderInfo;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import QimCommon.struts.AjaxActionSupport;
import pf.ProjectSettings;
import QimCommon.utils.IdWorker;
import QimCommon.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PayAction extends AjaxActionSupport {
    public String tradePay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            AliMerchantInfo merchantInfo = AliMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            TradePayRequestData tradePayRequestData = new TradePayRequestData();
            tradePayRequestData.app_id = merchantInfo.getAppid();
            tradePayRequestData.mchId = merchantInfo.getId();
            tradePayRequestData.scene = "bar_code";
            tradePayRequestData.auth_code = getParameter("auth_code").toString();
            tradePayRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
            tradePayRequestData.subject = getParameter("subject").toString();
            TradePay tradePay = new TradePay(tradePayRequestData, subMerchantUser.getId());
            tradePay.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey());
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("body",getParameter("subject").toString());
            resultMap.put("transaction_id",tradePay.getPayResponse().get("trade_no").toString());
            resultMap.put("out_trade_no", tradePay.getPayResponse().get("out_trade_no").toString());
            resultMap.put("total_fee", tradePay.getPayResponse().get("total_amount").toString());
            resultMap.put("time_end", tradePay.getPayResponse().get("gmt_payment").toString());
            return AjaxActionComplete(resultMap);
        } while (false);
        return AjaxActionComplete(false);
    }

    public String tradePreCreate() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            AliMerchantInfo merchantInfo = AliMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            TradePreCreateRequestData tradePreCreateRequestData = new TradePreCreateRequestData();
            tradePreCreateRequestData.app_id = merchantInfo.getAppid();
            tradePreCreateRequestData.total_amount = Double.parseDouble(getParameter("total_amount").toString());
            tradePreCreateRequestData.subject = getParameter("subject").toString();
            tradePreCreateRequestData.out_trade_no =  StringUtils.convertNullableString(getParameter("out_trade_no"),String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId()));
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "alipay/"
                    + CallbackAction.TRADEPRECREATE + "?mchId=" + merchantInfo.getId() + "&createUser=" + subMerchantUser.getId();
            tradePreCreateRequestData.notify_url = requestUrl;
            TradePreCreate tradePreCreate = new TradePreCreate(tradePreCreateRequestData);
            if (tradePreCreate.postRequest(merchantInfo.getPrivateKey(), merchantInfo.getPublicKey())) {
                if (StringUtils.convertNullableString(getParameter("auto_redirect")).compareTo("true") != 0) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("qr_code", tradePreCreate.getQrCode());
                    return AjaxActionComplete(resultMap);
                }
                else {
                    getResponse().sendRedirect(tradePreCreate.getQrCode());
                }
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public String queryOrder() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            AliOrderInfo orderInfo = AliOrderInfo.getOrderInfoByOrderNo(getParameter("out_trade_no").toString());
            List<AliOrderInfo> list = new ArrayList<>();
            list.add(orderInfo);
            return AjaxActionComplete(list);
        } while (false);

        return AjaxActionComplete(false);
    }
}
