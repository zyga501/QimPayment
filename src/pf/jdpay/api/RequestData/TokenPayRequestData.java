package pf.jdpay.api.RequestData;

public class TokenPayRequestData extends RequestData {
    public TokenPayRequestData() {
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

    public long expire;
    public String sign; //
    public String notify_url; //
    public double amount; //单位元
    public String trade_name ; //
    public String trade_describle ;
    public  String sub_mer ;
    public String term_no ;
    public String extra_info ;
}
