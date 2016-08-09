package com.database.weixin;

public class WxSubMerchantAct {
    public static void main(String[] args) throws Exception {
    }

    public WxSubMerchantAct getGoodstagById(long ID){
        String statement = "com.database.weixin.mapping.subMerchantAct.getSubMerchantActById";
        return Database.Instance().selectOne(statement, ID);
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
