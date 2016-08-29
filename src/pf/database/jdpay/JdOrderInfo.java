package pf.database.jdpay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdOrderInfo {
    public static boolean insertOrderInfo(Map param) {
        String statement = "pf.database.jdpay.mapping.orderInfo.insertOrderInfo";
        return Database.Instance().insert(statement, param) == 1;
    }
    public static Map getOrderInfoBytermno(String param) {
        String statement = "pf.database.jdpay.mapping.orderInfo.getOrderInfoBytermno";
        return Database.Instance().selectOne(statement, param);
    }
    public static List<HashMap> getJDOrderExpListByDate(String createuser, String startDate, String endDate) {
        String statement = "pf.database.jdpay.mapping.orderInfo.getOrderExpListByDate";
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("createuser",createuser);
        param.put("startdate",startDate);
        param.put("enddate",endDate);
        return Database.Instance().selectList(statement,param);
    }

}