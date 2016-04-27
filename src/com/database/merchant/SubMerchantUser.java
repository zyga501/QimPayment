package com.database.merchant;

import java.util.List;
import java.util.concurrent.Callable;

public class SubMerchantUser {
    public static void main(String[] args) throws Exception {
        SubMerchantUser subMerchantUser = new SubMerchantUser();
        SubMerchantUser.getSubMerchantUserBySubMerchantIdAndUserName(subMerchantUser);
    }

    public static SubMerchantUser getSubMerchantUserById(long id) {
        String statement = "com.database.merchant.mapping.subMerchantUser.getSubMerchantUserById";
        return Database.Instance().selectOne(statement, id);
    }

    public static SubMerchantUser getSubMerchantUserByAccount(SubMerchantUser subMerchantUser) {
        String statement = "com.database.merchant.mapping.subMerchantUser.getSubMerchantUserByAccount";
        return Database.Instance().selectOne(statement, subMerchantUser);
    }

    public static List<SubMerchantUser> getSubMerchantUserBySubMerchantId(long subMerchantId) {
        String statement = "com.database.merchant.mapping.subMerchantUser.getSubMerchantUserBySubMerchantId";
        return Database.Instance().selectList(statement, subMerchantId);
    }

    public static SubMerchantUser getSubMerchantUserBySubMerchantIdAndUserName(SubMerchantUser subMerchantUser) {
        String statement = "com.database.merchant.mapping.subMerchantUser.getSubMerchantUserBySubMerchantIdAndUserName";
        return Database.Instance().selectOne(statement, subMerchantUser);
    }

    public static boolean insertSubMerchantUserInfo(SubMerchantUser subMerchantUser, Callable<Boolean> callable) {
        if (getSubMerchantUserBySubMerchantIdAndUserName(subMerchantUser) == null) {
            String statement = "com.database.merchant.mapping.subMerchantUser.insertSubMerchantUserInfo";
            return Database.Instance().insert(statement, subMerchantUser, callable) == 1;
        }
        return false;
    }

    public static boolean updateWeixinIdById(SubMerchantUser subMerchantUser) {
        String statement = "com.database.merchant.mapping.subMerchantUser.updateWeixinIdById";
        return Database.Instance().update(statement, subMerchantUser) == 1;
    }

    public static boolean updateStoreNameById(SubMerchantUser subMerchantUser) {
        String statement = "com.database.merchant.mapping.subMerchantUser.updateStoreNameById";
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
