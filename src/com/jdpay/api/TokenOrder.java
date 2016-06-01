package com.jdpay.api;


import com.framework.utils.JsonUtils;
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
                if (responseResult.get("data").toString().contains("qrcode")) {
                    return true;
                }

        return false;
    }
}
