package com.database.jdpay;

import com.database.jdpay.*;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
    }

    public static MerchantInfo getMerchantInfoById(long id) {
        String statement = "com.database.jdpay.mapping.merchantInfo.getMerchantInfoById";
        return com.database.jdpay.Database.Instance().selectOne(statement, id);
    }

    public long getId() {
        return id_;
    }

    public void setId(long id_) {
        this.id_ = id_;
    }

    public String getPaycodemerchantno() {
        return paycodemerchantno_;
    }

    public void setPaycodemerchantno(String paycodemerchantno) {
        this.paycodemerchantno_ = paycodemerchantno;
    }

    public String getPaycodemd5key() {
        return paycodemd5key_;
    }

    public void setPaycodemd5key(String paycodemd5key) {
        this.paycodemd5key_ = paycodemd5key;
    }

    public String getScanmerchantno() {
        return scanmerchantno_;
    }

    public void setScanmerchantno(String scanmerchantno) {
        this.scanmerchantno_ = scanmerchantno;
    }

    public String getScanmd5key() {
        return scanmd5key_;
    }

    public void setScanmd5key(String scanmd5key) {
        this.scanmd5key_ = scanmd5key;
    }

    public long getMerchantid() {
        return merchantid_;
    }

    public void setMerchantid(long merchantid_) {
        this.merchantid_ = merchantid_;
    }

    private long id_;
    private long merchantid_;
    private String paycodemerchantno_;
    private String paycodemd5key_;
    private String scanmerchantno_;
    private String scanmd5key_;
}
