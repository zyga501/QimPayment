package com.merchant.database;

import java.util.concurrent.Callable;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {

    }

    public static SubMerchantInfo getSubMerchantInfoById(long id) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static SubMerchantInfo getSubMerchantInfoBySubId(String subId) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantInfoBySubId";
        return Database.Instance().selectOne(statement, subId);
    }

    public static byte[] getSubMerchantLogoById(long id) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantLogoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static boolean insertSubMerchantInfo(SubMerchantInfo subMerchantInfo, Callable<Boolean> callable) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.insertSubMerchantInfo";
        return Database.Instance().insert(statement, subMerchantInfo, callable) == 1;
    }

    public static boolean updateWeixinIdById(SubMerchantInfo subMerchantInfo) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateWeixinIdById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
    }

    public static boolean updateLogoById(SubMerchantInfo subMerchantInfo) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateLogoById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
    }

    public static boolean updateWeixinInfoById(SubMerchantInfo subMerchantInfo) {
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateWeixinInfoById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
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

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public long getSalemanId() {
        return salemanId_;
    }

    public void setSalemanId(long salemanId) {
        this.salemanId_ = salemanId;
    }

    public byte[] getLogo() {
        return logo_;
    }

    public void setLogo(byte[] logo) {
        this.logo_ = logo;
    }

    public String getAds() {
        return ads_;
    }

    public void setAds(String ads) {
        this.ads_ = ads;
    }

    public String getWeixinId() {
        return weixinId_;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId_ = weixinId;
    }

    public String getTemplateId() {
        return templateId_;
    }

    public void setTemplateId(String templateId) {
        this.templateId_ = templateId;
    }

    public String getAddress() {
        return address_;
    }

    public void setAddress(String address) {
        this.address_ = address;
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
    private String name_;
    private long salemanId_;
    private byte[] logo_;
    private String ads_;
    private String weixinId_;
    private String templateId_;
    private String address_;
    private String appid_;
    private String appsecret_;
}
