package com.merchant.database;

import org.apache.ibatis.session.SqlSession;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
        getSubMerchantLogoById(1596144145909760L);
    }

    public static SubMerchantInfo getSubMerchantInfoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantInfoById";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return subMerchantInfo;
    }

    public static SubMerchantInfo getSubMerchantInfoBySubId(String subId) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantInfoBySubId";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, subId);
        sqlSession.close();
        return subMerchantInfo;
    }

    public static byte[] getSubMerchantLogoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantLogoById";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return subMerchantInfo.getLogo();
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
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    private long id_;
    private long merchantId_;
    private String name_;
    private long salemanId_;
    private byte[] logo_;
    private String ads;

}
