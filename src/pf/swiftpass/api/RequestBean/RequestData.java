package pf.swiftpass.api.RequestBean;

import pf.swiftpass.utils.Signature;
import QimCommon.utils.StringUtils;

public class RequestData {
    public RequestData() {
        nonce_str = StringUtils.generateRandomString(32);
    }

    public boolean checkParameter() {
        try {
            return !service.isEmpty()
                    && !mch_id.isEmpty()
                    && !nonce_str.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildSign(String apiKey) throws IllegalAccessException {
        this.sign = Signature.generateSign(this, apiKey);
    }

    public String service; // 接口类型
    public String mch_id; // 商户号，由中信银行分配
    public String nonce_str; // 随机字符串，不长于32 位
    public String sign; // MD5 签名结果

    // option
    public String version; // 版本号，version 默认值是2.0
    public String charset; // 可选值UTF-8 ，默认为UTF-8
    public String sign_type; // 签名类型，取值：MD5 默认：MD5
    public String device_info; // 终端设备号
    public String attach; // 商户附加信息，可做扩展参数，255 字符内
}
