package pf.hgesy.api;

import QimCommon.utils.JsonUtils;
import pf.hgesy.api.RequestBean.ToPayRequestData;

import java.util.Map;

public class ToPay extends HgesyAPI {
    public final static String TOPAY_API = "http://www.hgesy.com:8080/PayMcc/gateway/to_pay";

    public ToPay(ToPayRequestData toPayRequestData) {
        requestData_ = toPayRequestData;
    }

    public String getQrCode() {
        return qrCode_;
    }

    @Override
    protected String getAPIUri() {
        String redirectUrl = TOPAY_API + "?";
        redirectUrl += requestData_.buildRequestData();
        return redirectUrl;
    }

    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        Map<String, Object> responseMap = JsonUtils.toMap(responseString, true);
        if (responseMap != null && responseMap.get("qr_code") != null) {
            qrCode_ = responseMap.get("qr_code").toString();
            return true;
        }
        return  false;
    }

    private ToPayRequestData requestData_;
    private String qrCode_;
}
