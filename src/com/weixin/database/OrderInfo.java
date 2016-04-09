package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class OrderInfo {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(true);
        String statement = "com.weixin.database.mapping.orderInfo.getOrderInfo";
        List<OrderInfo> orderInfos = sqlSession.selectList(statement);
        sqlSession.close();
    }

    public static OrderInfo getOrderInfoById(long id) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.orderInfo.getOrderInfoById";
        OrderInfo orderInfo = sqlSession.selectOne(statement, id);
        sqlSession.close();
        return orderInfo;
    }

    public static OrderInfo getOrderInfoByTransactionId(String transactionId) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.orderInfo.getOrderInfoByTransactionId";
        OrderInfo orderInfo = sqlSession.selectOne(statement, transactionId);
        sqlSession.close();
        return orderInfo;
    }

    public static boolean insertOrderInfo(OrderInfo orderInfo) {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(true);
        String statement = "com.weixin.database.mapping.orderInfo.insertOrderInfo";
        int result = sqlSession.insert(statement, orderInfo);
        sqlSession.commit();
        sqlSession.close();
        return result == 1;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getAppid() {
        return appid_;
    }

    public void setAppid(String appid) {
        this.appid_ = appid;
    }

    public String getMchId() {
        return mchId_;
    }

    public void setMchId(String mchId) {
        this.mchId_ = mchId;
    }

    public String getSubMchId() {
        return subMchId_;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId_ = subMchId;
    }

    public String getBody() {
        return body_;
    }

    public void setBody(String body) {
        this.body_ = body;
    }

    public String getTransactionId() {
        return transactionId_;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId_ = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo_;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo_ = outTradeNo;
    }

    public String getBankType() {
        return bankType_;
    }

    public void setBankType(String bankType) {
        this.bankType_ = bankType;
    }

    public int getTotalFee() {
        return totalFee_;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee_ = totalFee;
    }

    public String getTimeEnd() {
        return timeEnd_;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd_ = timeEnd;
    }

    public long getCreateUser() {
        return createUser_;
    }

    public void setCreateUser(long createUser) {
        this.createUser_ = createUser;
    }

    private long id_;
    private String appid_;
    private String mchId_;
    private String subMchId_;
    private String body_;
    private String transactionId_;
    private String outTradeNo_;
    private String bankType_;
    private int totalFee_;
    private String timeEnd_;
    private long createUser_;
}
