package com.database.merchant;

import com.database.jdpay.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
