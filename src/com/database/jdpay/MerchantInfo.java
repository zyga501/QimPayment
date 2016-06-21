package com.database.jdpay;

import net.sf.json.JSONObject;

public class MerchantInfo {
    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject("{tm:12}");
        System.out.println(jsonObject.get("term_no")==null?jsonObject.get("tm"):jsonObject.get("term_no"));
        System.out.println(jsonObject.get("tm"));
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

    public String getH5merchantno() {
        return h5merchantno_;
    }

    public void setH5merchantno(String h5merchantno_) {
        this.h5merchantno_ = h5merchantno_;
    }

    public String getH5md5key() {
        return h5md5key_;
    }

    public void setH5md5key(String h5md5key_) {
        this.h5md5key_ = h5md5key_;
    }

    public String getH5rsapublickey() {
        return h5rsapublickey_;
    }

    public void setH5rsapublickey(String h5RSAPublicKey_) {
        h5rsapublickey_ = h5RSAPublicKey_;
    }

    public String getH5rsaprivatekey() {
        return h5rsaprivatekey_;
    }

    public void setH5rsaprivatekey(String h5RSAPrivateKey_) {
        h5rsaprivatekey_ = h5RSAPrivateKey_;
    }

    private long id_;
    private long merchantid_;
    private String paycodemerchantno_;
    private String paycodemd5key_;
    private String scanmerchantno_;
    private String scanmd5key_;
    private String h5merchantno_;
    private String h5md5key_;
    private String h5rsapublickey_;
    private String h5rsaprivatekey_;

}