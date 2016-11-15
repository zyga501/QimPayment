package pf.swiftpass.action;

import framework.action.AjaxActionSupport;
import pf.ProjectLogger;

import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String WEIXINJSPAYCALLBACK = "Callback!weixinJsPay";
    public final static String ALIJSPAYCALLBACK = "Callback!aliJsPay";
    public final static String SUCCESS = "success";
    public final static String WEIXINPJSPAY = "SwiftPass.WeixinJsPay";
    public final static String ALIJSPAY = "SwiftPass.AliJsPay";

    public void weixinJsPay() throws Exception {
        handlerCallback(WEIXINPJSPAY);
        getResponse().getWriter().write(SUCCESS);
    }

    public void aliJsPay() throws Exception {
        handlerCallback(ALIJSPAY);
        getResponse().getWriter().write(SUCCESS);
    }

    private void handlerCallback(String tradeType) throws Exception {
        Map<String,Object> responseResult = getInputStreamMap();
        if (responseResult.get("result_code").toString().compareTo("0") == 0 &&
            responseResult.get("pay_result").toString().compareTo("0") == 0) {
            return;
        }

        ProjectLogger.error("Swiftpass Callback Error!");
    }
}
