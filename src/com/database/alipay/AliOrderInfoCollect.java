package com.database.alipay;

import com.database.merchant.OrderInfoCollect;

import java.util.HashMap;
import java.util.Map;

public class AliOrderInfoCollect extends OrderInfoCollect {
    public static com.database.merchant.OrderInfoCollect collectAliOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "com.database.alipay.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
