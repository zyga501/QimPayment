package pf.database.merchant;

import java.util.concurrent.Callable;

public class SubMerchantInfo {
    public static void main(String[] args) throws Exception {

    }

    public static SubMerchantInfo getSubMerchantInfoById(long id) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.getSubMerchantInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public static SubMerchantInfo getSubMerchantInfoBySubId(String subId) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.getSubMerchantInfoBySubId";
        return Database.Instance().selectOne(statement, subId);
    }

    public static byte[] getSubMerchantLogoById(long id) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.getSubMerchantLogoById";
        return ((SubMerchantInfo)Database.Instance().selectOne(statement, id)).getLogo();
    }

    public static SubMerchantInfo getSubMerchantInfoByAppId(String subId) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.getSubMerchantInfoByAppId";
        return Database.Instance().selectOne(statement, subId);
    }

    public static SubMerchantInfo getSubMerchantInfoByMerchantIdAndName(SubMerchantInfo subMerchantInfo) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.getSubMerchantInfoByMerchantIdAndName";
        return Database.Instance().selectOne(statement, subMerchantInfo);
    }

    public static boolean insertSubMerchantInfo(SubMerchantInfo subMerchantInfo, Callable<Boolean> callable) {
        if (getSubMerchantInfoByMerchantIdAndName(subMerchantInfo) == null) {
            String statement = "pf.database.merchant.mapping.subMerchantInfo.insertSubMerchantInfo";
            return Database.Instance().insert(statement, subMerchantInfo, callable) == 1;
        }
        return false;
    }

    public static boolean updateWeixinIdById(SubMerchantInfo subMerchantInfo) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.updateWeixinIdById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
    }

    public static boolean updateLogoById(SubMerchantInfo subMerchantInfo) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.updateLogoById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
    }

    public static boolean updateWeixinInfoById(SubMerchantInfo subMerchantInfo) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.updateWeixinInfoById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
    }

    public static boolean updateAdsById(SubMerchantInfo subMerchantInfo) {
        String statement = "pf.database.merchant.mapping.subMerchantInfo.updateAdsById";
        return Database.Instance().update(statement, subMerchantInfo) == 1;
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

    public String getAppsecret() {
            return appsecret_;
    }

    public void setAppsecret(String appsecret_) {
        this.appsecret_ = appsecret_;
    }

    public String getAppid() {
        return appid_;
    }

    public void setAppid(String appid_) {
        this.appid_ = appid_;
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
