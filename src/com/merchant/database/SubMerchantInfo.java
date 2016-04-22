package com.merchant.database;

import com.framework.ProjectSettings;
import com.framework.utils.IdWorker;
import org.apache.ibatis.session.SqlSession;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {
        SubMerchantInfo subMerchantInfo = new SubMerchantInfo();
        subMerchantInfo.setId(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        subMerchantInfo.setMerchantId(1596082254858240L);
        subMerchantInfo.setName("1234");
        subMerchantInfo.setAddress("123");
        SubMerchantInfo.insertSubMerchantInfo(subMerchantInfo);
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

    public static SqlSession insertSubMerchantInfo(SubMerchantInfo subMerchantInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.insertSubMerchantInfo";
        int result = sqlSession.insert(statement, subMerchantInfo);
        if (result == 1) {
            return sqlSession;
        }
        else {
            sqlSession.close();
            return null;
        }
    }

    public static boolean updateWeixinIdById(SubMerchantInfo subMerchantInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateWeixinIdById";
        int result = sqlSession.update(statement, subMerchantInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
    }

    public static boolean updateLogoById(SubMerchantInfo subMerchantInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateLogoById";
        int result = sqlSession.update(statement, subMerchantInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
    }

    public static boolean updateWeixinInfoById(SubMerchantInfo subMerchantInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantInfo.updateWeixinInfoById";
        int result = sqlSession.update(statement, subMerchantInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
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
