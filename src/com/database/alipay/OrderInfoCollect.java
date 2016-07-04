package com.database.alipay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfoCollect extends com.database.merchant.OrderInfoCollect {
    public static com.database.merchant.OrderInfoCollect collectAliOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "com.database.alipay.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
