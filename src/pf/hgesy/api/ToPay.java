package pf.hgesy.api;

import pf.hgesy.api.RequestBean.ToPayRequestData;

public class ToPay extends HgesyAPI {
    public final static String TOPAY_API = "http://www.hgesy.com:8080/PayMcc/gateway/direct_pay";

    @Override
    protected String getAPIUri() {
        return TOPAY_API;
    }
}
