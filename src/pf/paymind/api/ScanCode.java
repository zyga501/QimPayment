package pf.paymind.api;

import pf.paymind.api.RequestBean.JsPayRequestData;
import pf.paymind.api.RequestBean.ScanCodeRequestData;

public class ScanCode extends JsPay {
    public final static String SCANCODE_API = "http://real.izhongyin.com/middlepaytrx/wx/scanCode";

    public ScanCode(ScanCodeRequestData scanCodeRequestData) {
        super(scanCodeRequestData);
    }

    @Override
    protected String getAPIUri() {
        return SCANCODE_API;
    }
}
