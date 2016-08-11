package com.weixin.api.RequestData;

import com.framework.utils.StringUtils;
import com.weixin.utils.Signature;

public class RequestData {
    public RequestData() {
        nonce_str = StringUtils.generateRandomString(32);
    }

    public boolean checkParameter() {
        try {
            return !appid.isEmpty()
                    && !mch_id.isEmpty()
                    && !sub_mch_id.isEmpty()
                    && !nonce_str.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildSign(String apiKey) throws IllegalAccessException {
        this.sign = Signature.generateSign(this, apiKey);
    }

    public String appid; // 公众账号ID 微信分配的公众账号ID
    public String mch_id; // 商户号 微信支付分配的商户号
    public String sub_mch_id; // 子商户号 微信支付分配的子商户号，开发者模式下必填
    public String nonce_str; // 随机字符串，不长于32位。
    public String sign; // 签名

    //option
    public String sub_appid; // 子商户公众账号ID 微信分配的子商户公众账号ID
}
