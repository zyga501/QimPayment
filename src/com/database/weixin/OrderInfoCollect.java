package com.database.weixin;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfoCollect extends com.database.merchant.OrderInfoCollect {
    public static com.database.merchant.OrderInfoCollect collectOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "com.database.weixin.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
