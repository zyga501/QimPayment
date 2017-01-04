package pf.api.mode;

import QimCommon.utils.StringUtils;
import pf.ProjectLogger;
import pf.weixin.utils.Signature;

public class HgesyMode extends BaseMode {
    private final static String WeixinToPay = "HgesyWeixinToPay";
    private final static String AliToPay = "HgesyAliToPay";
    private final static String JsPay = "HgesyJsPay";

    public String scanPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("HgesyMode.scanPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        if (StringUtils.convertNullableString(requestBuffer_.get("method")).toLowerCase().compareTo("alipay.scanpay") == 0) {
            return AliToPay;
        }

        return WeixinToPay;
    }

    public String jsPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("HgesyMode.jsPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        return JsPay;
    }
}
