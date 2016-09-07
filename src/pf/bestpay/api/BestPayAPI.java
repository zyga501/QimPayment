package pf.bestpay.api;

import pf.framework.utils.HttpClient;

public abstract class BestPayAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        return true;
    }
}
