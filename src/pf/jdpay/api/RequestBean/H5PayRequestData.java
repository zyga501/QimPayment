package pf.jdpay.api.RequestBean;

public class H5PayRequestData extends RequestData {
    public H5PayRequestData() {
    }

    public boolean checkParameter() {
        try {
            return  !order_no.isEmpty()
                    && !merchant_no.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String sign; //
    public String notify_url; //
    public double amount; //单位元
    public  String currency ;
    public String note ;
}