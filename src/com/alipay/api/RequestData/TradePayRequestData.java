package com.alipay.api.RequestData;

import com.framework.ProjectSettings;
import com.framework.utils.IdWorker;

public class TradePayRequestData extends RequestData {
    TradePayRequestData () {
        if (out_trade_no == null) {
            out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        }
    }

    public String out_trade_no; // 商户订单号,64个字符以内
    public String scene; // 支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code
    public String auth_code; // 支付授权码
    public String subject;
    public String body;

    // option
    public String seller_id; // 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
    public double total_amount; // 订单总金额，单位为元，精确到小数点后两位
    public double discountable_amount; // 参与优惠计算的金额
    public double undiscountable_amount; // 不参与优惠计算的金额
    public String goods_detail; // 订单包含的商品列表信息，Json格式
    public String operator_id;
    public String store_id;
    public String terminal_id;
    public String sys_service_provider_id;
    public String timeout_express;
    public String royalty_info;
}
