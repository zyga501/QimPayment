package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantInfo.getSubMerchantInfoById";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, (Object) 1596144145909760L);
        sqlSession.close();
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getMerchantId() {
        return merchantId_;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId_ = merchantId;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getSubId() {
        return subId_;
    }

    public void setSubId(String subId) {
        this.subId_ = subId;
    }

    public long getSalemanId() {
        return salemanId_;
    }

    public void setSalemanId(long salemanId) {
        this.salemanId_ = salemanId;
    }

    private long id_;
    private String merchantId_;
    private String name_;
    private String subId_;
    private long salemanId_;
}
