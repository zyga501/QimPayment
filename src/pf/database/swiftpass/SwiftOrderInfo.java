package pf.database.swiftpass;

public class SwiftOrderInfo {
    public static SwiftOrderInfo getOrderInfoByOrderNo(String orderNo) {
        String statement = "pf.database.swiftpass.mapping.orderInfo.getOrderInfoByTradeNo";
        return Database.Instance().selectOne(statement, orderNo);
    }

    public static boolean insertOrderInfo(SwiftOrderInfo swiftOrderInfo) {
        String statement = "pf.database.swiftpass.mapping.orderInfo.insertOrderInfo";
        return Database.Instance().insert(statement, swiftOrderInfo) == 1;
    }

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

    public String getOutTradeNo() {
        return outTradeNo_;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo_ = outTradeNo;
    }

    public int getTotalFee() {
        return totalFee_;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee_ = totalFee;
    }

    public String getTimeEnd() {
        return timeEnd_;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd_ = timeEnd;
    }

    int id_;
    private long mchId_;
    private String outTradeNo_;
    private int totalFee_;
    private String timeEnd_;
}
