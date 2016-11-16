package pf.database.switftpass;

public class SwiftOrderInfo {
    public int getId() {
        return id_;
    }

    public void setId(int id) {
        id_ = id;
    }

    public long getMchId() {
        return mchId_;
    }

    public void setMchId(long mchId) {
        this.mchId_ = mchId;
    }

    public String getOut_trade_no() {
        return out_trade_no_;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no_ = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee_;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee_ = total_fee;
    }

    public String getTime_end() {
        return time_end_;
    }

    public void setTime_end(String time_end) {
        this.time_end_ = time_end;
    }

    int id_;
    private long mchId_;
    private String out_trade_no_;
    private int total_fee_;
    private String time_end_;
}
