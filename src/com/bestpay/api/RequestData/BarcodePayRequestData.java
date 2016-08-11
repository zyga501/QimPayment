package com.bestpay.api.RequestData;

import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;
import com.framework.utils.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BarcodePayRequestData extends RequestData {
    public BarcodePayRequestData() {
        orderReqNo = orderNo = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        orderDate = df.format(new Date(Long.valueOf(System.currentTimeMillis()).longValue()));
    }

    @Override
    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !orderNo.isEmpty()
                    && !orderReqNo.isEmpty()
                    && !orderDate.isEmpty()
                    && !barCode.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    @Override
    public void buildMac(String keyString) {
        StringBuilder stringBuilder = new StringBuilder();//组装mac加密明文串
        stringBuilder.append("MERCHANTID=").append(merchantId);
        stringBuilder.append("&ORDERNO=").append(orderNo);
        stringBuilder.append("&ORDERREQNO=").append(orderReqNo);
        stringBuilder.append("&ORDERDATE=").append(orderDate);
        stringBuilder.append("&BARCODE=").append(barCode);
        stringBuilder.append("&ORDERAMT=").append(orderAmt);
        stringBuilder.append("&KEY=").append(keyString); //此处是商户的key
        this.mac = MD5.MD5Encode(stringBuilder.toString());
    }

    public String orderNo; // 订单号
    public String orderReqNo; // 订单请求交易流水号,与订单号一致
    public String orderDate; // 订单日期
    public String barCode; // 条形码号
    public int orderAmt; // 单位：分
}
