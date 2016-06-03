package com.jdpay.api.RequestData;

public class QueryRequestData extends RequestData {
    public QueryRequestData() {
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


//    public String order_no ;
//    public String merchant_no  ;

}
