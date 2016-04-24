package com.database.weixin;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
    }

    public static SubMerchantInfo getSubMerchantInfoById(long id) {
        String statement = "com.database.weixin.mapping.subMerchantInfo.getSubMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static SubMerchantInfo getSubMerchantInfoBySubId(String subId) {
        String statement = "com.database.weixin.mapping.subMerchantInfo.getSubMerchantInfoBySubId";
        return Database.Instance().selectOne(statement, subId);
    }

    public static boolean insertSubMerchantInfo(SubMerchantInfo subMerchantInfo) {
        String statement = "com.database.weixin.mapping.subMerchantInfo.insertSubMerchantInfo";
        return Database.Instance().insert(statement, subMerchantInfo) == 1;
    }

    public static long getSubMerchantIdByCompatibleId(String compatibleId) {
        String statement = "com.database.weixin.mapping.subMerchantInfo.getSubMerchantIdByCompatibleId";
        return Database.Instance().selectOne(statement, compatibleId);
    }

    public static SubMerchantInfo getSubMerchantInfoByAppId(String subId) {
        String statement = "com.database.weixin.mapping.subMerchantInfo.getSubMerchantInfoByAppId";
        return Database.Instance().selectOne(statement, subId);
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

    private long id_;
    private long merchantId_;
    private String subId_;
    private String appid_;
    private String appsecret_;
}
