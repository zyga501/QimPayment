package com.database.jdpay;

import java.util.HashMap;
import java.util.Map;

public class OrderInfoCollect extends com.database.merchant.OrderInfoCollect {
    public static com.database.merchant.OrderInfoCollect collectJDOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "com.database.jdpay.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
