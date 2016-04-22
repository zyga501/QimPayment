package com.merchant.database;

import com.framework.ProjectSettings;
import com.framework.database.DatabaseAction;
import com.framework.utils.IdWorker;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubMerchantUser {
    public static void main(String[] args) throws Exception {

    }

    public static SubMerchantUser getSubMerchantUserById(long id) {
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserById";
        return Database.Instance().selectOne(statement, id);
    }

    public static SubMerchantUser getSubMerchantUserByLogin(String subMerchantId, String userName, String userPwd) {
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserByAccount";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("submerchantid", subMerchantId);
        param.put("userName", userName);
        param.put("userPwd", userPwd);
        return Database.Instance().selectOne(statement, param);
    }

    public static List<SubMerchantUser> getSubMerchantUserBySubMerchantId(long subMerchantId) {
        String statement = "com.merchant.database.mapping.subMerchantUser.getSubMerchantUserBySubMerchantId";
        return Database.Instance().selectList(statement, subMerchantId);
    }

    public static boolean insertSubMerchantUserInfo(SubMerchantUser subMerchantUser, DatabaseAction databaseAction) {
        String statement = "com.merchant.database.mapping.subMerchantUser.insertSubMerchantUserInfo";
        return Database.Instance().insert(statement, subMerchantUser, databaseAction) == 1;
    }

    public static boolean updateWeixinIdById(SubMerchantUser subMerchantUser) {
        String statement = "com.merchant.database.mapping.subMerchantUser.updateWeixinIdById";
        return Database.Instance().update(statement, subMerchantUser) == 1;
    }

    public static boolean updateStoreNameById(SubMerchantUser subMerchantUser) {
        String statement = "com.merchant.database.mapping.subMerchantUser.updateStoreNameById";
        return Database.Instance().update(statement, subMerchantUser) == 1;
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
