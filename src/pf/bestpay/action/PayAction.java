package pf.bestpay.action;

import pf.bestpay.api.BarcodePayAPI;
import pf.bestpay.api.OrderPayAPI;
import pf.bestpay.api.RequestBean.BarcodePayRequestData;
import pf.bestpay.api.RequestBean.OrderPayRequestData;
import pf.bestpay.utils.LedgerUtils;
import pf.database.bestpay.BtMerchantInfo;
import pf.database.bestpay.BtSubMerchantInfo;
import pf.database.bestpay.LedgerInfo;
import pf.database.merchant.SubMerchantUser;
import pf.framework.action.AjaxActionSupport;
import pf.framework.utils.ClassUtils;

import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String barcodePay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            BtSubMerchantInfo subMerchantInfo = BtSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            BtMerchantInfo merchantInfo = BtMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

            BarcodePayRequestData barcodePayRequestData = new BarcodePayRequestData();
            barcodePayRequestData.merchantId = merchantInfo.getMchId();
            barcodePayRequestData.subMerchantId = subMerchantInfo.getSubId();
                    barcodePayRequestData.barcode = getParameter("barcode").toString();
            if (getParameter("orderNo") != null) {
                barcodePayRequestData.orderReqNo = barcodePayRequestData.orderNo = getParameter("orderNo").toString();
            }
            barcodePayRequestData.orderAmt = barcodePayRequestData.productAmt = Integer.parseInt(getParameter("productAmt").toString());
            if (getParameter("attachAmt") != null) {
                barcodePayRequestData.attachAmt = Integer.parseInt(getParameter("attachAmt").toString());
                barcodePayRequestData.orderAmt += barcodePayRequestData.attachAmt;
            }
            barcodePayRequestData.storeId = "000000";
            LedgerInfo ledgerInfo = LedgerInfo.getLedgerInfoById(subMerchantUser.getSubMerchantId());
            if (ledgerInfo != null) {
                barcodePayRequestData.ledgerDetail = LedgerUtils.buildLedgerInfo(
                        subMerchantInfo.getSubId(),
                        barcodePayRequestData.orderAmt,
                        merchantInfo.getRate(),
                        merchantInfo.getMchId(),
                        ledgerInfo.getMerchantRate(),
                        ledgerInfo.getSubMerchantRates()
                        );
            }
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "bestpay/" + CallbackAction.BARCODEPAY;
            barcodePayRequestData.backUrl = requestUrl;

            BarcodePayAPI barcodePay = new BarcodePayAPI(barcodePayRequestData);
            return AjaxActionComplete(barcodePay.postRequest(merchantInfo.getDataKey()));
        } while (false);

        return AjaxActionComplete(false);
    }

    public String orderPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            BtSubMerchantInfo subMerchantInfo = BtSubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            BtMerchantInfo merchantInfo = BtMerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
            if (merchantInfo == null) {
                break;
            }

            OrderPayRequestData orderPayRequestData = new OrderPayRequestData();
            orderPayRequestData.merchantId = orderPayRequestData.subMerchantId = merchantInfo.getMchId();
            if (getParameter("orderSeq") != null) {
                orderPayRequestData.orderSeq = orderPayRequestData.orderReqTranSeq = getParameter("orderNo").toString();
            }
            orderPayRequestData.orderAmt = orderPayRequestData.productAmt = Integer.parseInt(getParameter("productAmt").toString());
            if (getParameter("productDesc") != null) {
                orderPayRequestData.productDesc = getParameter("productDesc").toString();
            }
            orderPayRequestData.productDesc = "企盟H5支付";
            if (getParameter("productDesc") != null) {
                orderPayRequestData.productDesc = getParameter("productDesc").toString();
            }
            if (getParameter("attach") != null) {
                orderPayRequestData.attach = getParameter("attach").toString();
            }
            LedgerInfo ledgerInfo = LedgerInfo.getLedgerInfoById(subMerchantUser.getSubMerchantId());
            if (ledgerInfo != null) {
                orderPayRequestData.divDetail = LedgerUtils.buildLedgerInfo(
                        subMerchantInfo.getSubId(),
                        orderPayRequestData.orderAmt,
                        merchantInfo.getRate(),
                        merchantInfo.getMchId(),
                        ledgerInfo.getMerchantRate(),
                        ledgerInfo.getSubMerchantRates()
                );
            }

            OrderPayAPI orderPay = new OrderPayAPI(orderPayRequestData);
            if (orderPay.postRequest(merchantInfo.getDataKey())) {
                Map<String, Object> resultMap = ClassUtils.convertToMap(orderPayRequestData);
                resultMap.put("key", merchantInfo.getDataKey());
                resultMap.put("merchantPwd", merchantInfo.getMchPwd());
                resultMap.put("sign", orderPayRequestData.buildSign(merchantInfo.getMchPwd()));
                String requestUrl = getRequest().getRequestURL().toString();
                requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "bestpay/" + CallbackAction.ORDERPAY;
                resultMap.put("backMerchantUrl", requestUrl);
                return AjaxActionComplete(true, resultMap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }
}
