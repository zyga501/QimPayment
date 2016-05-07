package com.alipay.api.RequestData;

public class RequestData {
    RequestData() {
        charset = "utf-8";
        sign_type = "RSA";
        timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        version = "1.0";
    }

    public boolean checkParameter() {
        try {
            return !app_id.isEmpty()
                    && !method.isEmpty()
                    && !charset.isEmpty()
                    && !sign_type.isEmpty()
                    && !sign.isEmpty()
                    && !timestamp.isEmpty()
                    && !version.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String app_id; // 支付宝分配给开发者的应用Id
    public String method; // 接口名称
    public String charset; // 请求使用的编码格式
    public String sign_type; // 商户生成签名字符串所使用的签名算法类型
    public String sign; // 商户请求参数的签名串
    public String timestamp; // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
    public String version; // 调用的接口版本，固定为：1.0

    // option
    public String app_auth_token; // 详见应用授权概述
}
