package com.bestpay.action;

import com.bestpay.api.BarcodePay;
import com.bestpay.api.OrderPay;
import com.bestpay.api.RequestData.BarcodePayRequestData;
import com.bestpay.api.RequestData.OrderPayRequestData;
import com.database.bestpay.BtMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.ClassUtils;

import java.util.Map;

public class PayAction extends AjaxActionSupport {
    public String barcodePay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            BtMerchantInfo merchantInfo = BtMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (merchantInfo == null) {
                break;
            }

            BarcodePayRequestData barcodePayRequestData = new BarcodePayRequestData();
            barcodePayRequestData.subMerchantId = barcodePayRequestData.merchantId = merchantInfo.getMchId();
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

            BarcodePay barcodePay = new BarcodePay(barcodePayRequestData);
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

            BtMerchantInfo merchantInfo = BtMerchantInfo.getMerchantInfoById(subMerchantUser.getSubMerchantId());
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
            orderPayRequestData.productDesc = "下单";
            if (getParameter("productDesc") != null) {
                orderPayRequestData.productDesc = getParameter("productDesc").toString();
            }
            if (getParameter("attach") != null) {
                orderPayRequestData.attach = getParameter("attach").toString();
            }

            OrderPay orderPay = new OrderPay(orderPayRequestData);
            if (orderPay.postRequest(merchantInfo.getDataKey())) {
                Map<String, Object> resultMap = ClassUtils.convertToMap(orderPayRequestData);
                resultMap.put("merchantPwd", merchantInfo.getMchPwd());
                return AjaxActionComplete(true, resultMap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }
}
