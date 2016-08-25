package com.bestpay.api.RequestData;

import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;
import com.framework.utils.MD5;
import com.framework.utils.StringUtils;

public class OrderPayRequestData extends RequestData {
    public OrderPayRequestData() {
        orderSeq = orderReqTranSeq = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        orderReqTime = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
        transCode = "01";
        attach = "";
    }

    public boolean checkParameter() {
        try {
            return !orderSeq.isEmpty()
                    && !orderSeq.isEmpty()
                    && !orderReqTime.isEmpty()
                    && !transCode.isEmpty()
                    && orderAmt != 0;
        }
        catch (Exception exception) {

        }

        return false;
    }

    @Override
    public void buildMac(String keyString) {
        StringBuilder stringBuilder = new StringBuilder();//组装mac加密明文串
        stringBuilder.append("MERCHANTID=").append(merchantId);
        stringBuilder.append("&ORDERSEQ=").append(orderSeq);
        stringBuilder.append("&ORDERREQTRANSEQ=").append(orderReqTranSeq);
        stringBuilder.append("&ORDERREQTIME=").append(orderReqTime);
        stringBuilder.append("&KEY=").append(keyString); //此处是商户的key
        this.mac = MD5.MD5Encode(stringBuilder.toString()).toUpperCase();
    }

    public String buildSign(String merchantPwd) {
        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("SERVICE=").append("mobile.security.pay")
                .append("&MERCHANTID=").append(merchantId)
                .append("&MERCHANTPWD=").append(merchantPwd)
                .append("&SUBMERCHANTID=").append(subMerchantId)
                .append("&BACKMERCHANTURL=").append("")
                .append("&ORDERSEQ=").append(orderSeq)
                .append("&ORDERREQTRANSEQ=").append(orderReqTranSeq)
                .append("&ORDERTIME=").append(orderReqTime)
                .append("&ORDERVALIDITYTIME=").append("")
                .append("&CURTYPE=").append("RMB")
                .append("&ORDERAMOUNT=").append(orderAmt * 0.01)
                .append("&SUBJECT=").append("")
                .append("&PRODUCTID=").append("04")
                .append("&PRODUCTDESC=").append(productDesc)
                .append("&CUSTOMERID=").append("")
                .append("&SWTICHACC=").append("true");
        return MD5.MD5Encode(stringBuilder.toString());
    }

    public String orderSeq; // 订单号
    public String orderReqTranSeq; // 订单请求流水号
    public String orderReqTime; // yyyyMMDDhhmmss
    public String transCode; // 交易代码
    public int productAmt; // 产品金额
    public int orderAmt; // 订单金额（分）
    public String productDesc; // 商品描述
    public String attach; // 附加信息

    // option
    public String orderCCY; // 币种补充
    public String serviceCode; // 接入渠道
    public String productId; // 商品代码
    public String loginNo; // 翼支付登录账号
    public String provinceCode; // 省份
    public String cityCode; // 城市
    public String divDetail; // 分账信息
    public String encodeType; // MAC字段的加密方式
    public String sessionKey; // 登录密串
    public String encode; // 加密因子索引
}
