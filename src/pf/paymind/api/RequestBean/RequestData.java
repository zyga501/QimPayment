package pf.paymind.api.RequestBean;

public class RequestData {
    public RequestData() {

    }

    public String trxType; // 接口类型
    public String merchantNo; // 商户编号
    public String orderIp; // 用户支付IP
    public String encrypt; // T0/T1标识，若此项为T0，对应的5,11,12,13,14必填
    public String sign; // 签名

    // option
    public String goodsName; // 订单描述
    public String timeOut; // 订单失效时间
    public String toibkn; // 收款行联行号
    public String callNo; // 入账卡号
    public String idCardNo; // 入帐卡对应身份证号
    public String payerName; // 入帐卡对应姓名
}
