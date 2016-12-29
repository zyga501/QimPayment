package pf.api.mode;

import QimCommon.utils.StringUtils;
import pf.ProjectLogger;
import pf.weixin.utils.Signature;

public class PayMindMode extends BaseMode {
    private final static String WeixinScanCode = "PayMindWeixinScanCode";
    private final static String WeixinJsPay = "PayMindWeixinJsPay";

    public String scanPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("PayMindMode.scanPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        return WeixinScanCode;
    }

    public String jsPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("PayMindMode.jsPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        return WeixinJsPay;
    }
}
