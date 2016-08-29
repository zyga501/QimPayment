package pf.database.weixin;

import pf.database.merchant.OrderInfoCollect;

import java.util.HashMap;
import java.util.Map;

public class WxOrderInfoCollect extends OrderInfoCollect {
    public static OrderInfoCollect collectOrderInfoByDate(String createUser, String startDate, String endDate) {
        String statement = "pf.database.weixin.mapping.orderInfo.collectOrderInfoByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createUser", createUser);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        return Database.Instance().selectOne(statement,param);
    }

}
