package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoByAppId";
        MerchantInfo merchantInfo = Database.Instance().selectOne(statement, "wx0bfa8f7ec59b1f33");

    }

    public static List<MerchantInfo> getAllMerchantInfo() {
        String statement = "com.weixin.database.mapping.merchantInfo.getAllMerchantInfo";
        return Database.Instance().selectList(statement);
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static MerchantInfo getMerchantInfoByAppId(String appid) {
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoByAppId";
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

    private Long id_;
    private String appid_;
    private String appsecret_;
    private String mchId_;
    private String apiKey_;
}
