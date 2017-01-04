package pf.paymind.api.RequestBean;

public class RequestData {
    public RequestData() {

    }

    public boolean checkParameter() {
        try {
            return !trxType.isEmpty()
                    && !merchantNo.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildSign(String apiKey) throws IllegalAccessException {

    }

    public String buildRequestData() {
        return "";
    }

    public String trxType; // 接口类型
    public String merchantNo; // 商户编号
    public String sign; // 签名

    // option
    public String goodsName; // 订单描述
    public String timeOut; // 订单失效时间
    public String toibkn; // 收款行联行号
    public String callNo; // 入账卡号
    public String idCardNo; // 入帐卡对应身份证号
    public String payerName; // 入帐卡对应姓名
}
