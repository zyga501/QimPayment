package pf.api.mode;

import pf.ProjectLogger;
import pf.weixin.utils.Signature;

public class HgesyMode extends BaseMode {
    private final static String HgesyJsPay = "HgesyJsPay";

    public String jsPay() {
        if (!Signature.checkSignValid(requestBuffer_, requestBuffer_.get("id").toString())) {
            ProjectLogger.error("HgesyMode.jsPay.checkSignValid.Failed!");
            return super.jsPay();
        }

        return HgesyJsPay;
    }
}
