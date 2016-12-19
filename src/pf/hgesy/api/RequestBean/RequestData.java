package pf.hgesy.api.RequestBean;

import pf.hgesy.utils.Signature;

public class RequestData {
    public boolean checkParameter() {
        return true;
    }

    public void buildSign(String apiKey) throws IllegalAccessException {
        this.sign = Signature.generateSign(this, apiKey);
    }

    public String buildRequestData() {
        return "";
    }

    public String sign;
}
