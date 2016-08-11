package com.weixin.api.RequestData;

import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UnifiedOrderRequestData extends RequestData {
    public UnifiedOrderRequestData() throws UnknownHostException {
        spbill_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            boolean parameterValid = !body.isEmpty()
                    && !out_trade_no.isEmpty()
                    && total_fee >= 1
                    && !spbill_create_ip.isEmpty()
                    && !notify_url.isEmpty()
                    && !trade_type.isEmpty();
            switch (trade_type) {
                case "JSAPI": {
                    if (openid != null && !openid.isEmpty()) {
                        parameterValid = parameterValid && true;
                    }
                    else if (sub_openid != null && !sub_openid.isEmpty()) {
                        parameterValid = parameterValid && !sub_appid.isEmpty();
                    }
                    else {
                        parameterValid = parameterValid && false;
                    }
                    break;
                }
                case "NATIVE": {
                    parameterValid = parameterValid && !product_id.isEmpty();
                    break;
                }
                case "APP": {
                    break;
                }
            }

            return parameterValid;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public String body; // 商品描述 商品或支付单简要描述
    public String out_trade_no; // 商户订单号 商户系统内部的订单号,32个字符内、可包含字母
    public Integer total_fee; // 总金额 订单总金额，单位为分，只能为整数
    public String spbill_create_ip; // 调用微信支付API的机器IP
    public String auth_code; // 授权码 扫码支付授权码，设备读取用户微信中的条码或者二维码信息
    public String notify_url; // 通知地址 接收微信支付异步通知回调地址
    public String trade_type; // 交易类型 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付

    // option
    public String device_info; // 设备号 终端设备号(商户自定义，如门店编号)
    public String detail; // 商品详情 商品名称明细列表
    public String attach; // 附加数据 在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    public String fee_type; // 货币类型 符合ISO 4217标准的三位字母代码，默认人民币：CNY
    public String limit_pay; // 指定支付方式 no_credit--指定不能使用信用卡支付
    public String time_start; // 交易起始时间 订单生成时间，格式为yyyyMMddHHmmss
    public String time_expire; // 交易结束时间 订单失效时间，格式为yyyyMMddHHmmss
    public String product_id; // 商品ID trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义
    public String openid; // 用户标识 trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    public String sub_openid; // 用户子标识 trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
}
