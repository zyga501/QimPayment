package com.database.jdpay;

import java.util.Map;

public class OrderInfo {
    public static boolean insertOrderInfo(Map param) {
        String statement = "com.database.jdpay.mapping.orderInfo.insertOrderInfo";
        return Database.Instance().insert(statement, param) == 1;
    }
    public static Map getOrderInfoBytermno(String param) {
        String statement = "com.database.jdpay.mapping.orderInfo.getOrderInfoBytermno";
        return Database.Instance().selectOne(statement, param);
    }
}