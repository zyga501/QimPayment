package pf.database.swiftpass;

public class SwiftMerchantInfo {
    public static SwiftMerchantInfo getMerchantInfoById(long id) {
        String statement = "pf.database.swiftpass.mapping.merchantInfo.getMerchantInfoById";
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

    public String getAliAppId() {
        return aliAppId_;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId_ = aliAppId;
    }

    public String getAliPrivateKey() {
        return aliPrivateKey_;
    }

    public void setAliPrivateKey(String aliPrivateKey) {
        this.aliPrivateKey_ = aliPrivateKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey_;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey_ = aliPublicKey;
    }

    private long id_;
    private String mchId_;
    private String apiKey_;
    private String aliAppId_;
    private String aliPrivateKey_;
    private String aliPublicKey_;
}
