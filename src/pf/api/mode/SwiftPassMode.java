package pf.api.mode;

import framework.utils.StringUtils;
import pf.ProjectLogger;
import pf.weixin.utils.Signature;

public class SwiftPassMode extends BaseMode {
    private final static String WeixinJsPay = "SwiftPassWeixinJsPay";
    private final static String AliJsPay = "SwiftPassAliJsPay";

    public String jsPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("SwiftPassMode.jsPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        if (StringUtils.convertNullableString(requestBuffer_.get("method")).toLowerCase().compareTo("alipay.jspay") == 0) {
            return AliJsPay;
        }

        return WeixinJsPay;
    }
}
