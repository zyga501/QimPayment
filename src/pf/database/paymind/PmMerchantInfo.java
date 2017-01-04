package pf.database.paymind;

public class PmMerchantInfo {
    public static PmMerchantInfo getMerchantInfoById(long id) {
        String statement = "pf.database.paymind.mapping.merchantInfo.getMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getMchId() {
        return mchId_;
    }

    public void setMchId(String mchId) {
        this.mchId_ = mchId;
    }

    public String getApiKey() {
        return apiKey_;
    }

    public void setApiKey(String apiKey) {
        this.apiKey_ = apiKey;
    }

    private long id_;
    private String mchId_;
    private String apiKey_;
}
