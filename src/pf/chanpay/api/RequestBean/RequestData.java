package pf.chanpay.api.RequestBean;

public class RequestData {
    public String trxCode; // 报文交易代码 标识此请求为“单笔垫资付款业务”
    public String mertid; // 商户代码 标识商户的唯一ID，15位
    public String reqSn; // 交易请求号 数据格式：(15位)商户号 + (12位)yyMMddHHmmss时间戳 + (5位)循环递增序号 = (32位)唯一交易号；
    public String timestamp; // 受理时间 代支付系统接收到交易请求时服务器时间； 对于交易的发起时间以此时间为准
    public String retCode; // CJ返回代码
    public String errMsg; // 错误信息
    public String subMertid; // 二级商户号
}
