package com.api.test.RequestData;

public class MicroPayRequestData {
    public String id;
    public String mode;
    public String body; // 商品描述 商品或支付单简要描述
    public Integer total_fee; // 总金额 订单总金额，单位为分，只能为整数
    public String auth_code; // 授权码 扫码支付授权码，设备读取用户微信中的条码或者二维码信息
    public String sign;
}
