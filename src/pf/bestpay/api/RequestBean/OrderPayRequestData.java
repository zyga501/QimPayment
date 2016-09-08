package pf.bestpay.api.RequestBean;

import pf.framework.base.ProjectSettings;
import pf.framework.utils.IdWorker;
import pf.framework.utils.MD5;
import pf.framework.utils.StringUtils;

public class OrderPayRequestData extends RequestData {
    public OrderPayRequestData() {
        orderSeq = orderReqTranSeq = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        orderReqTime = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
        transCode = "01";
        attach = "";
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

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
        stringBuilder.append("&MERCHANTID=").append(merchantId)
                .append("&ORDERNO=").append(orderSeq)
                .append("&ORDERREQNO=").append(orderReqTranSeq)
                .append("&ORDERDATA=").append(orderReqTime)
                .append("&OURTRANSNO=").append(orderSeq)
                .append("&TRANSAMT=").append(orderAmt * 0.01)
                .append("&TRANSSTATUS=").append("B")
                .append("&ENCODETYPE=").append("1")
                .append("&MERCHANTPWD=").append(merchantPwd);
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
