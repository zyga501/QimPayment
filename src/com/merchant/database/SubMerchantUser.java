package com.merchant.database;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubMerchantUser {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserById";
        SubMerchantUser subMerchantUser = sqlSession.selectOne(statement, (Object) 1596144387655680L);
        sqlSession.close();
    }

    public static SubMerchantUser getSubMerchantUserById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserById";
        SubMerchantUser subMerchantUser = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return subMerchantUser;
    }

    public static SubMerchantUser getSubMerchantUserByLogin(String subMerchantId, String userName, String userPwd) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserByAccount";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("submerchantid", subMerchantId);
        param.put("userName", userName);
        param.put("userPwd", userPwd);
        SubMerchantUser subMerchantUser = sqlSession.selectOne(statement, param);
        sqlSession.close();
        return subMerchantUser;
    }

    public static List<SubMerchantUser> getSubMerchantUserBySubMerchantId(long subMerchantId) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserBySubMerchantId";
        List<SubMerchantUser> subMerchantUserList = sqlSession.selectList(statement, subMerchantId);
        sqlSession.close();
        return subMerchantUserList;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public long getSubMerchantId() {
        return subMerchantId_;
    }

    public void setSubMerchantId(long subMerchantId) {
        this.subMerchantId_ = subMerchantId;
    }

    public String getUserName() {
        return userName_;
    }

    public void setUserName(String userName) {
        this.userName_ = userName;
    }

    public String getUserPwd() {
        return userPwd_;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd_ = userPwd;
    }

    public String getStoreName() {
        return storeName_;
    }

    public void setStoreName(String storeName) {
        this.storeName_ = storeName;
    }

    public String getWeixinId() {
        return weixinId_;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId_ = weixinId;
    }

    private long id_;
    private long subMerchantId_;
    private String userName_;
    private String userPwd_;
    private String storeName_;
    private String weixinId_;
}
