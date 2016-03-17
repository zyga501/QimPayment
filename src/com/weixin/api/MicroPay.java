package com.weixin.api;

import java.util.SortedMap;
import java.util.TreeMap;

public class MicroPay extends WeixinAPI {
    public static String MICROPAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

    public class MicroPayInfo extends CommonInfo {
        SortedMap<String, String> convertToMap() {
            SortedMap<String, String> fields = new TreeMap<String, String>();
            fields.put("appid", appid);
            fields.put("mch_id", mch_id);
            fields.put("nonce_str", nonce_str);
            fields.put("body", body);
            fields.put("out_trade_no", "SK" + out_trade_no);//订单号
            fields.put("total_fee", total_fee.toString());
            fields.put("goods_tag", goods_tag);
            fields.put("spbill_create_ip", spbill_create_ip);
            fields.put("auth_code", auth_code);
            fields.put("sub_mch_id", sub_mch_id);
            return fields;
        }
        String device_info; // 设备号 终端设备号(商户自定义，如门店编号)
        String body; // 商品描述 商品或支付单简要描述
        String detail; // 商品详情 商品名称明细列表
        String attach; // 附加数据 在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        String out_trade_no; // 商户订单号 商户系统内部的订单号,32个字符内、可包含字母
        Integer total_fee; // 总金额 订单总金额，单位为分，只能为整数
        String fee_type; // 货币类型 符合ISO 4217标准的三位字母代码，默认人民币：CNY
        String spbill_create_ip; // 调用微信支付API的机器IP
        String goods_tag; // 商品标记 商品标记，代金券或立减优惠功能的参数
        String limit_pay; // 指定支付方式 no_credit--指定不能使用信用卡支付
        String auth_code; // 授权码 扫码支付授权码，设备读取用户微信中的条码或者二维码信息
    }

    public MicroPay(MicroPayInfo microPayInfo) {
        microPayInfo_ = microPayInfo;
    }

    @Override
    protected SortedMap<String, String> buildRequestParameter() {
        return microPayInfo_.convertToMap();
    }

    @Override
    protected String getAPIUri() {
        return MICROPAY_API;
    }

    private MicroPayInfo microPayInfo_;
}
