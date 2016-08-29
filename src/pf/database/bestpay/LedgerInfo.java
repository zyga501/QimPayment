package pf.database.bestpay;

public class LedgerInfo {
    public static LedgerInfo getLedgerInfoById(long id) {
        String statement = "pf.database.bestpay.mapping.ledgerInfo.getLedgerInfoById";
        return Database.Instance().selectOne(statement, id);
    }

    public Long getId() {
        return id_;
    }

    public void setId(Long id) {
        this.id_ = id;
    }

    public double getMerchantRate() {
        return merchantRate_;
    }

    public void setMerchantRate(double merchantRate) {
        this.merchantRate_ = merchantRate;
    }

    public String getSubMerchantRates() {
        return subMerchantRates_;
    }

    public void setSubMerchantRates(String subMerchantRates) {
        this.subMerchantRates_ = subMerchantRates;
    }

    private Long id_;
    private double merchantRate_;
    private String subMerchantRates_;
}
