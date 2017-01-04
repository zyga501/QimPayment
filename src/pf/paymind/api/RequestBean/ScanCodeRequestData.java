package pf.paymind.api.RequestBean;

public class ScanCodeRequestData extends JsPayRequestData {
    public ScanCodeRequestData() throws Exception {
        trxType = "WX_SCANCODE";
    }
}
