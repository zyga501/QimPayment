package pf.alipay.api.RequestBean;

public class RoyaltyInfo {
    public RoyaltyDetailInfos royalty_detail_infos; // 分账明细的信息，可以描述多条分账指令，json数组

    // option
    public String royalty_typel; // 分账类型 卖家的分账类型
}