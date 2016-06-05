package com.jdpay.api.RequestData;

import com.framework.utils.StringUtils;

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

}
