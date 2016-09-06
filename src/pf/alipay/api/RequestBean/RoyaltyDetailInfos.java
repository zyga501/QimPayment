package pf.alipay.api.RequestBean;

public class RoyaltyDetailInfos {
    public String batch_no;
    public String trans_out_type;
    public String trans_out;
    public String trans_in;
    public double amount;

    // option
    public int serial_no; // 分账序列号，表示分账执行的顺序，必须为正整数
    public String trans_in_type;
    public String out_relation_id;
    public String desc;
}
