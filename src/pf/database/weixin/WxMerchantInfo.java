package pf.database.weixin;

import java.util.List;

public class WxMerchantInfo {
    public static void main(String[] args) throws Exception {
        String statement = "pf.database.weixin.mapping.merchantInfo.getMerchantInfoByAppId";
        WxMerchantInfo merchantInfo = Database.Instance().selectOne(statement, "wx0bfa8f7ec59b1f33");
    }

    public static List<WxMerchantInfo> getAllMerchantInfo() {
        String statement = "pf.database.weixin.mapping.merchantInfo.getAllMerchantInfo";
        return Database.Instance().selectList(statement);
    }

    public static WxMerchantInfo getMerchantInfoById(long id) {
        String statement = "pf.database.weixin.mapping.merchantInfo.getMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static WxMerchantInfo getMerchantInfoByAppId(String appid) {
        String statement = "pf.database.weixin.mapping.merchantInfo.getMerchantInfoByAppId";
        return Database.Instance().selectOne(statement, appid);
    }

    public Long getId() {
        return id_;
    }

    public void setId(Long id) {
        this.id_ = id;
    }

    public String getAppid() {
        return appid_;
    }

    public void setAppid(String appid) {
        this.appid_ = appid;
    }

    public String getAppsecret() {
        return appsecret_;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret_ = appsecret;
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

    public String getTemplateId() {
        return templateid_;
    }

    public void setTemplateId(String templateid_) {
        this.templateid_ = templateid_;
    }

    private Long id_;
    private String appid_;
    private String appsecret_;
    private String mchId_;
    private String apiKey_;
    private String templateid_;
}
