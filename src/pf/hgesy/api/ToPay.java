package pf.hgesy.api;

import pf.hgesy.api.RequestBean.ToPayRequestData;

public class ToPay extends HgesyAPI {
    public final static String TOPAY_API = "http://www.hgesy.com:8080/PayMcc/gateway/to_pay";

    public ToPay(ToPayRequestData toPayRequestData) {
        requestData_ = toPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        String redirectUrl = TOPAY_API + "?";
        redirectUrl += requestData_.buildRequestData();
        return redirectUrl;
    }

    private ToPayRequestData requestData_;
}
