package pf.jdpay.api.RequestData;

public class MicroPayRequestData extends RequestData {
    public MicroPayRequestData() {
    }

    public boolean checkParameter() {
        try {
            return  !merchant_no.isEmpty()
                    && !order_no.isEmpty()
                    && !seed.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String seed;
    public String sign; //
    public String notify_url; //
    public double amount; //单位元
    public String trade_name ; //
    public String trade_describle ;
    public  String sub_mer ;
    public String term_no ;
    public String extra_info ;
}
