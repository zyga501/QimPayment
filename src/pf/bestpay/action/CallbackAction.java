package pf.bestpay.action;

import pf.framework.action.AjaxActionSupport;

public class CallbackAction extends AjaxActionSupport {
    public final static String BARCODEPAY = "CALLBACK!BARCODEPAY";
    public final static String ORDERPAY  = "CALLBACK!ORDERPAY";

    public void BARCODEPAY() throws Exception {
        if (getParameter("UPTRANSEQ") != null) {
            getResponse().getWriter().write("UPTRANSEQ_" + getParameter("UPTRANSEQ").toString());
        }
    }

    public void ORDERPAY() throws Exception {
        if (getParameter("UPTRANSEQ") != null) {
            getResponse().getWriter().write("UPTRANSEQ_" + getParameter("UPTRANSEQ").toString());
        }
    }
}
