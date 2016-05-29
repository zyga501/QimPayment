package com.jdpay.api.RequestData;

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

}
