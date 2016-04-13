package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoByAppId";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, "wx0bfa8f7ec59b1f33");
        sqlSession.close();
    }

    public static List<MerchantInfo> getAllMerchantInfo() {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.merchantInfo.getAllMerchantInfo";
        List<MerchantInfo> merchantInfoList = sqlSession.selectList(statement);
        sqlSession.close();
        return merchantInfoList;
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoById";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return merchantInfo;
    }

    public static MerchantInfo getMerchantInfoByAppId(String appid) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.merchantInfo.getMerchantInfoByAppId";
        MerchantInfo merchantInfo = sqlSession.selectOne(statement, "wx0bfa8f7ec59b1f33");
        sqlSession.close();
        return merchantInfo;
    }

    public static boolean updateAccessToken(Long id, String accessToken) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(true);
        String statement = "com.weixin.database.mapping.merchantInfo.updateAccessToken";
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setId(id);
        merchantInfo.setAccessToken(accessToken);
        int result = sqlSession.update(statement, merchantInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
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

    public String getAccessToken() {
        return accessToken_;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken_ = accessToken;
    }

    private Long id_;
    private String appid_;
    private String appsecret_;
    private String mchId_;
    private String apiKey_;
    private String accessToken_;
}
