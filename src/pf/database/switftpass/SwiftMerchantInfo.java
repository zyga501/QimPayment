package pf.database.switftpass;

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

    public String getWeixinAppId() {
        return weixinAppId_;
    }

    public void setWeixinAppId(String weixinAppId) {
        this.weixinAppId_ = weixinAppId;
    }

    public String getWeixinAppSecret() {
        return weixinAppSecret_;
    }

    public void setWeixinAppSecret(String weixinAppSecret) {
        this.weixinAppSecret_ = weixinAppSecret;
    }

    public String getAliAppId() {
        return aliAppId_;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId_ = aliAppId;
    }

    public String getAliprivateKey() {
        return aliprivateKey_;
    }

    public void setAliprivateKey(String aliprivateKey) {
        this.aliprivateKey_ = aliprivateKey;
    }

    public String getAlipublicKey() {
        return alipublicKey_;
    }

    public void setAlipublicKey(String alipublicKey) {
        this.alipublicKey_ = alipublicKey;
    }

    private long id_;
    private String mchId_;
    private String apiKey_;
    private String weixinAppId_;
    private String weixinAppSecret_;
    private String aliAppId_;
    private String aliprivateKey_;
    private String alipublicKey_;
}
