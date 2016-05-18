package com.alipay.api.RequestData;

import com.framework.ProjectSettings;
import com.framework.utils.ClassUtils;
import com.framework.utils.IdWorker;
import net.sf.json.JSONArray;

public class TradePreCreateRequestData extends RequestData {
    public TradePreCreateRequestData() {
        if (out_trade_no == null) {
            out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        }
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !out_trade_no.isEmpty()
                    && !subject.isEmpty()
                    && !notify_url.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    @Override
    public String buildRequestData() {
        String requestData = JSONArray.fromObject(ClassUtils.convertToMap(this, false)).toString().substring(1);
        return requestData.substring(0, requestData.length() - 1);
    }

    public String out_trade_no; // 商户订单号,64个字符以内
    public String subject; // 订单标题

    // option
    public String buyer_logon_id;
    public String body; // 订单描述
    public String seller_id; // 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
    public double total_amount; // 订单总金额，单位为元，精确到小数点后两位
    public String goods_detail; // 订单包含的商品列表信息，Json格式
    public String operator_id; // 商户操作员编号
    public String store_id; // 商户门店编号
    public String terminal_id; // 商户机具终端编号
    public String extend_params; // 业务扩展参数
    public String timeout_express; // 该笔订单允许的最晚付款时间，逾期将关闭交易
    public RoyaltyInfo royalty_info; // 描述分账信息，Json格式，其它说明详见分账说明
}
