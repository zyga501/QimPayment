package pf.weixin.api.RequestBean;

public class RefundRequestData extends RequestData {
    public RefundRequestData() {

    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return (!transaction_id.isEmpty() || !out_trade_no.isEmpty())
                    && !out_refund_no.isEmpty()
                    && total_fee >= 1
                    && refund_fee >= 1
                    && !op_user_id.isEmpty();
        }
        catch (Exception exception) {
            return false;
        }
    }

    public String transaction_id; // 微信订单号
    public String out_trade_no; // 商户订单号 商户系统内部的订单号, transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no
    public String out_refund_no; // 商户退款单号
    public int total_fee; // 总金额
    public int refund_fee; // 退款金额
    public String op_user_id; // 操作员

    // option
    public String device_info; // 设备号 终端设备号(商户自定义，如门店编号)
    public String refund_fee_type; // 货币种类
}
