package pf.weixin.api;

import pf.framework.utils.HttpClient;

public abstract class WeixinAPI extends HttpClient {
    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        return true;
    }
}
