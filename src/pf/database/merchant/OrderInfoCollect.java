package pf.database.merchant;

public class OrderInfoCollect
{
    public int getInfoCount() {
        return infoCount_;
    }

    public void setInfoCount(int count) {
        this.infoCount_ = count;
    }

    public double getSumFee() {
        return sumFee_;
    }

    public void setSumFee(double sumFee) {
        this.sumFee_ = sumFee;
    }

    private int infoCount_;
    private double sumFee_;
}
