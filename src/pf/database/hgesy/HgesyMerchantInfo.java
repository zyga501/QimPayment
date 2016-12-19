package pf.database.hgesy;

public class HgesyMerchantInfo {
    public static HgesyMerchantInfo getMerchantInfoById(long id) {
        String statement = "pf.database.hgesy.mapping.merchantInfo.getMerchantInfoById";
        return pf.database.swiftpass.Database.Instance().selectOne(statement, id);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getAccount() {
        return account_;
    }

    public void setAccount(String account) {
        this.account_ = account;
    }

    public String getApiKey() {
        return apiKey_;
    }

    public void setApiKey(String apiKey) {
        this.apiKey_ = apiKey;
    }

    private long id_;
    private String account_;
    private String apiKey_;
}
