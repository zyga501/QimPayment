package com.database.alipay;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
        String statement = "com.database.alipay.mapping.merchantInfo.getMerchantInfoById";
        MerchantInfo merchantInfo = Database.Instance().selectOne(statement, 1629719047391232L);
        System.out.print(merchantInfo.getPrivateKey());
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        String statement = "com.database.alipay.mapping.merchantInfo.getMerchantInfoById";
        return com.database.weixin.Database.Instance().selectOne(statement, id);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getAppid() {
        return appid_;
    }

    public void setAppid(String appid) {
        this.appid_ = appid;
    }

    public String getPrivateKey() {
        return privateKey_;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey_ = privateKey;
    }

    public String getPublicKey() {
        return publicKey_;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey_ = publicKey;
    }

    private long id_;
    public String appid_;
    private String privateKey_;
    private String publicKey_;
}
