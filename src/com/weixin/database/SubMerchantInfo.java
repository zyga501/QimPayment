package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
    }

    public static SubMerchantInfo getSubMerchantInfoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantInfo.getSubMerchantInfoById";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return subMerchantInfo;
    }

    public static SubMerchantInfo getSubMerchantInfoBySubId(String subId) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantInfo.getSubMerchantInfoBySubId";
        SubMerchantInfo subMerchantInfo = sqlSession.selectOne(statement, subId);
        sqlSession.close();
        return subMerchantInfo;
    }

    public static boolean insertSubMerchantInfo(SubMerchantInfo subMerchantInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantInfo.insertSubMerchantInfo";
        int result = sqlSession.insert(statement, subMerchantInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
    }

    public static long getSubMerchantIdByCompatibleId(String compatibleId) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantInfo.getSubMerchantIdByCompatibleId";
        long subMerchantId = sqlSession.selectOne(statement, compatibleId);
        sqlSession.close();
        return subMerchantId;
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

    private long id_;
    private long merchantId_;
    private String subId_;
}
