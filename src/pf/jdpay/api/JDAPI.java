package pf.jdpay.api;

import pf.framework.utils.HttpClient;

public abstract class JDAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        return true;
    }
}