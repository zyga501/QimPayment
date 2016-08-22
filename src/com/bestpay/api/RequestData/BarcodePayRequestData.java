package com.bestpay.api.RequestData;

import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;
import com.framework.utils.MD5;
import com.framework.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class BarcodePayRequestData extends RequestData {
    public BarcodePayRequestData() {
        orderReqNo = orderNo = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        orderDate = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
        TransType = "B";
        channel = "05";
        busiType = "0000001";
    }

    @Override
    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !channel.isEmpty()
                    && !busiType.isEmpty()
                    && !orderNo.isEmpty()
                    && !orderReqNo.isEmpty()
                    && !orderDate.isEmpty()
                    && !barcode.isEmpty()
                    && !storeId.isEmpty()
                    && orderAmt != 0
                    && productAmt != 0;
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
        stringBuilder.append("&BARCODE=").append(barcode);
        stringBuilder.append("&ORDERAMT=").append(orderAmt);
        stringBuilder.append("&KEY=").append(keyString); //此处是商户的key
        this.mac = MD5.MD5Encode(stringBuilder.toString());
    }

    public String TransType; //
    public String channel; // 渠道
    public String busiType; // 业务类型
    public String orderNo; // 订单号
    public String orderReqNo; // 订单请求交易流水号,与订单号一致
    public String orderDate; // 订单日期
    public String barcode; // 条形码号
    public int orderAmt; // 单位：分. 订单总金额 = 产品金额+附加金额
    public int productAmt; // 产品金额
    public int attachAmt; // 附加金额
    public String storeId; // 门店号

    // option
    public String goodsName; // 商品名称
    public String backUrl; // 商户提供的用于异步接收交易返回结果的后台url，若不需要后台返回，可不填，若需要后台返回，请保障地址可用
    public String ledgerDetail; // 商户需要在结算时进行分账情况，需填写此字段，详情见接口说明分账明细
    /*
        商户若需分账，则填写ledgerDetail字段，例如：
        a) 规则：023101111:4|023102222:3|023103333:1
        b) 说明：订单总金额4+3+1=8分,商户023101111分账4分，每组对应关系之间“|”分隔
        c) 分账权限与一般支付权限不同。商户分账支付的权限需要额外申请，并且在结算时翼支付将形成各个分账商户的对账文件。
    */
    public String attach; // 商户附加信息
}
