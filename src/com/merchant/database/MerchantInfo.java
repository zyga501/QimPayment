package com.merchant.database;

import org.apache.ibatis.session.SqlSession;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.merchantInfo.getMerchantInfoByAppId";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, "wx0bfa8f7ec59b1f33");
        sqlSession.close();
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.merchantInfo.getMerchantInfoById";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return merchantInfo;
    }

    public static MerchantInfo getMerchantInfoByAppId(String appid) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.merchantInfo.getMerchantInfoByAppId";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, "wx0bfa8f7ec59b1f33");
        sqlSession.close();
        return merchantInfo;
    }

    public Long getId() {
        return id_;
    }

    public void setId(Long id) {
        this.id_ = id;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    private Long id_;
    private String name_;
}
