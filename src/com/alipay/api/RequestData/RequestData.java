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

    public String app_id;
    public String method;
    public String charset;
    public String sign_type;
    public String sign;
    public String timestamp;
    public String version;

    // option
    public String app_auth_token;
}
