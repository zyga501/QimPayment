package com.jdpay.api;


import com.jdpay.api.RequestData.TokenPayRequestData;

import java.util.Map;

public class TokenOrder extends JDAPIWithSign {
    public final static String GETCODEURL = "https://payscc.jdpay.com/req/code";

    public TokenOrder(TokenPayRequestData tokenPayRequestData) {
        requestData_ = tokenPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return GETCODEURL;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        switch (responseResult.get("trade_type").toString()) {
            case "NATIVE": {
                if (responseResult.get("code_url") != null) {
                    return true;
                }
            }
            case "JSAPI": {
                if (responseResult.get("prepay_id") != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
