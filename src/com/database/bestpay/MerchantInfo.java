package com.database.bestpay;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        String statement = "com.database.bestpay.mapping.merchantInfo.getMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public Long getId() {
        return id_;
    }

    public void setId(Long id) {
        this.id_ = id;
    }

    public String getMchId() {
        return mchId_;
    }

    public void setMchId(String mchId) {
        this.mchId_ = mchId;
    }

    private Long id_;
    private String mchId_;
}
