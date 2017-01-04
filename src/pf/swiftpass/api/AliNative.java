package pf.swiftpass.api;

import pf.swiftpass.api.RequestBean.AliNativeRequestData;

import java.util.Map;

public class AliNative extends SwiftPassAPIWithSign {
    public final static String NATIVE_API = "https://pay.swiftpass.cn/pay/gateway";

    public AliNative(AliNativeRequestData aliNativeRequestData) {
        requestData_ = aliNativeRequestData;
    }

    public String getCodeUrl() {
        return codeUrl_;
    }

    @Override
    protected String getAPIUri() {
        return NATIVE_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        if (responseResult.get("code_url") != null) {
            codeUrl_ = responseResult.get("code_url").toString();
            return true;
        }
        return false;
    }

    private String codeUrl_;
}
