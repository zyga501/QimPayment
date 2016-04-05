package com.merchant.database;

import org.apache.ibatis.session.SqlSession;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.getSubMerchantInfoById";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, 1596144145909760L);
        sqlSession.close();
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

    private long id_;
    private long merchantId_;
    private String name_;
    private long salemanId_;
}
