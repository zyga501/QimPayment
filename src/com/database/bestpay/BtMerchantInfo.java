package com.database.bestpay;

public class BtMerchantInfo {
    public static void main(String[] args) throws Exception {
    }

    public static BtMerchantInfo getMerchantInfoById(long id) {
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

    public String getMchPwd() {
        return mchPwd_;
    }

    public void setMchPwd(String mchPwd) {
        this.mchPwd_ = mchPwd;
    }

    public String getDataKey() {
        return dataKey_;
    }

    public void setDataKey(String dataKey) {
        this.dataKey_ = dataKey;
    }

    private Long id_;
    private String mchId_;
    private String mchPwd_;
    private String dataKey_;
}
