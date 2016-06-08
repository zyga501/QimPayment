package com.jdpay.api;


import com.jdpay.api.RequestData.H5PayRequestData;

import java.util.Map;

public class H5Pay extends JDAPIWithSign {
    public final static String H5PAYURL = "https://payscc.jdpay.com/order/h5/create";

    public H5Pay(H5PayRequestData h5PayRequestData) {
        requestData_ = h5PayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return H5PAYURL;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        return true;
    }
}