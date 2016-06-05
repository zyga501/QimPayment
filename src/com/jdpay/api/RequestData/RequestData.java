package com.jdpay.api.RequestData;

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

    public String merchant_no; // 商户号 微信支付分配的商户号
    public String order_no; // 随机字符串，不长于32位。
    public String sign; //
    public String notify_url; //
    public double amount; //单位元
    public String trade_name ; //
    public String trade_describle ;
    public  String sub_mer ;
    public String term_no ;
    public String extra_info ;

}
