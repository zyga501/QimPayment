package pf.database.bestpay;

public class BtSubMerchantInfo {
    public static BtSubMerchantInfo getSubMerchantInfoById(long id) {
        String statement = "pf.database.bestpay.mapping.subMerchantInfo.getSubMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public long getMerchantId() {
        return merchantId_;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId_ = merchantId;
    }

    public String getSubId() {
        return subId_;
    }

    public void setSubId(String subId) {
        this.subId_ = subId;
    }

    private long id_;
    private long merchantId_;
    private String subId_;
}
