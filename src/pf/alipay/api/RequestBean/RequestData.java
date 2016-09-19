package pf.alipay.api.RequestBean;

import net.sf.json.JSONArray;
import pf.alipay.utils.Signature;
import framework.utils.ClassUtils;
import framework.utils.StringUtils;

public class RequestData {
    public RequestData() {
        charset = "utf-8";
        sign_type = "RSA";
        timestamp = StringUtils.generateDate("yyyy-MM-dd HH:mm:ss", "GMT+8");
        version = "1.0";
        alipay_sdk = "alipay-sdk-java-dynamicVersionNo";
        format = "json";
    }

    public RequestData(RequestData requestData) {
        this.app_id = requestData.app_id;
        this.method = requestData.method;
        this.charset = requestData.charset;
        this.sign = requestData.sign;
        this.sign_type = requestData.sign_type;
        this.timestamp = requestData.timestamp;
        this.version = requestData.version;
        this.notify_url = requestData.notify_url;
        this.biz_content = requestData.biz_content;
        this.alipay_sdk = requestData.alipay_sdk;
        this.format = requestData.format;
    }

    public boolean checkParameter() {
        try {
            return !app_id.isEmpty()
                    && !method.isEmpty()
                    && !charset.isEmpty()
                    && !sign_type.isEmpty()
                    && !timestamp.isEmpty()
                    && !version.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildRequestData() {
        String requestData = JSONArray.fromObject(ClassUtils.convertToMap(this, false)).toString().substring(1);
        biz_content = requestData.substring(0, requestData.length() - 1);
    }

    public void buildSign(String privateKey) throws IllegalAccessException {
        sign = Signature.generateSign(new RequestData(this), privateKey);
    }

    public String app_id; // 支付宝分配给开发者的应用Id
    public String method; // 接口名称
    public String charset; // 请求使用的编码格式
    public String sign_type; // 商户生成签名字符串所使用的签名算法类型
    public String sign; // 商户请求参数的签名串
    public String timestamp; // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
    public String version; // 调用的接口版本，固定为：1.0
    public String notify_url;
    public String biz_content;
    public String alipay_sdk;
    public String format;

    // option
    public String app_auth_token; // 详见应用授权概述
}
