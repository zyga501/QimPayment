package pf.api.mode;

import QimCommon.utils.StringUtils;
import pf.ProjectLogger;
import pf.weixin.utils.Signature;

public class SwiftPassMode extends BaseMode {
    private final static String WeixinNative = "SwiftPassWeixinNative";
    private final static String AliNative = "SwiftPassAliNative";
    private final static String WeixinJsPay = "SwiftPassWeixinJsPay";
    private final static String AliJsPay = "SwiftPassAliJsPay";

    public String scanPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("WeixinMode.scanPay.checkSignValid.Failed!");
            return super.scanPay();
        }

        if (StringUtils.convertNullableString(requestBuffer_.get("method")).toLowerCase().compareTo("alipay.scanpay") == 0) {
            return AliNative;
        }

        return WeixinNative;
    }

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
