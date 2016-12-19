package pf.hgesy.action;

import QimCommon.struts.AjaxActionSupport;

public class CallbackAction extends AjaxActionSupport {
    public final static String HGESYCALLBACKSUCCESS = "SUCCESS";
    public final static Object syncObject = new Object();

    public void directPay() throws Exception {
        synchronized (syncObject) {

        }

        getResponse().getWriter().write(HGESYCALLBACKSUCCESS);
    }
}
