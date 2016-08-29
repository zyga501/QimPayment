package pf.alipay.api.RequestData;

public class QueryData extends RequestData {
    public QueryData(RequestData requestData) {
        this.app_id = requestData.app_id;
        this.method = requestData.method;
        this.charset = requestData.charset;
        this.sign = requestData.sign;
        this.sign_type = requestData.sign_type;
        this.timestamp = requestData.timestamp;
        this.version = requestData.version;
        this.notify_url = requestData.notify_url;
        this.alipay_sdk = requestData.alipay_sdk;
        this.format = requestData.format;
    }
}
