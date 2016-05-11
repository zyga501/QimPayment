package com.alipay.api.RequestData;

import com.framework.ProjectSettings;
import com.framework.utils.ClassUtils;
import com.framework.utils.IdWorker;
import net.sf.json.JSONArray;

public class TradePayRequestData extends RequestData {
    public class RoyaltyDetailInfos {
        public String batch_no;
        public String trans_out_type;
        public String trans_out;
        public String trans_in;
        public double amount;

        // option
        public int serial_no; // 分账序列号，表示分账执行的顺序，必须为正整数
        public String trans_in_type;
        public String out_relation_id;
        public String desc;
    }

    public class RoyaltyInfo {
        public RoyaltyDetailInfos royalty_detail_infos; // 分账明细的信息，可以描述多条分账指令，json数组

        // option
        public String royalty_typel; // 分账类型 卖家的分账类型
    }

    public TradePayRequestData () {
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
                    && !scene.isEmpty()
                    && !auth_code.isEmpty()
                    && !sign_type.isEmpty()
                    && !subject.isEmpty();
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
    public String scene; // 支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code
    public String auth_code; // 支付授权码
    public String subject; // 订单标题

    // option
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
