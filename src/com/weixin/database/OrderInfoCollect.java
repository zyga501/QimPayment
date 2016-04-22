package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class OrderInfoCollect
{
    public static void main(String[] args) throws Exception {
        collectOrderInfoByDate("1596144387655680", "2016-04-02", "2016-04-04");
    }

    public static OrderInfoCollect collectOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "com.weixin.database.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

    public int getInfoCount() {
        return infoCount_;
    }

    public void setInfoCount(int count) {
        this.infoCount_ = count;
    }

    public int getSumFee() {
        return sumFee_;
    }

    public void setSumFee(int sumFee) {
        this.sumFee_ = sumFee;
    }

    private int infoCount_;
    private int sumFee_;
}
