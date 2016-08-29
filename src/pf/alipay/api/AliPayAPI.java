package pf.alipay.api;

import pf.framework.utils.HttpClient;

public abstract class AliPayAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String... args) throws Exception {
        return true;
    }
}
