package pf.alipay.api;

import pf.alipay.api.RequestData.TradePreCreateRequestData;

import java.util.Map;

public class TradePreCreate extends AliPayAPIWithSign {
    public final static String TRADEPAY_API = "https://openapi.alipay.com/gateway.do";

    public TradePreCreate(TradePreCreateRequestData requestData) {
        requestData_ = requestData;
        requestData_.method = "alipay.trade.precreate";
    }

    public String getQrCode() { return qrCode_; }

    @Override
    protected String getAPIUri() {
        return TRADEPAY_API;
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        if (responseResult.containsKey("alipay_trade_precreate_response")) {
            Map<String, Object> response = (Map<String, Object>)responseResult.get("alipay_trade_precreate_response");
            if (response.containsKey("qr_code")) {
                qrCode_ = response.get("qr_code").toString();
                return true;
            }
        }
        return false;
    }

    private String qrCode_;
}
