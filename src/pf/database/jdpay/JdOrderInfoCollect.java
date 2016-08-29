package pf.database.jdpay;

import pf.database.merchant.OrderInfoCollect;

import java.util.HashMap;
import java.util.Map;

public class JdOrderInfoCollect extends OrderInfoCollect {
    public static OrderInfoCollect collectJDOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "pf.database.jdpay.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
