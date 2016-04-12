package com.weixin.database;

import org.apache.ibatis.session.SqlSession;

public class SubMerchantAct {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantAct.getSubMerchantActById";
        SubMerchantAct subMerchantAct = sqlSession.selectOne(statement, (Object) 1596144145909760L);
        sqlSession.close();
    }

    public SubMerchantAct getGoodstagById(long ID){
        SqlSession sqlSession = Database.SqlSessionFactory().openSession();
        String statement = "com.weixin.database.mapping.subMerchantAct.getSubMerchantActById";
        SubMerchantAct subMerchantAct = sqlSession.selectOne(statement, ID);
        sqlSession.close();
        return subMerchantAct;
    }

    public long getId() {
        return id_;
    }

    public void setId(long id) {
        this.id_ = id;
    }

    public String getGoodsTag() {
        return goodsTag_;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag_ = goodsTag;
    }

    private long id_;
    private String goodsTag_;
}
