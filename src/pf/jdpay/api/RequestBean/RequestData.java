package pf.jdpay.api.RequestBean;

import pf.jdpay.utils.Signature;

public class RequestData {
    public RequestData() {
    }

    public boolean checkParameter() {
        try {
            return  !merchant_no.isEmpty()
                    && !order_no.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildSign(String apiKey) throws Exception {
        this.sign = Signature.generateSign(this, apiKey);
    }

    public String merchant_no; // 商户号 微信支付分配的商户号
    public String order_no; // 随机字符串，不长于32位。
    public String sign; //

}
