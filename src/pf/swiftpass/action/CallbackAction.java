package pf.swiftpass.action;

import framework.action.AjaxActionSupport;
import framework.utils.StringUtils;
import framework.utils.XMLParser;
import pf.ProjectLogger;
import pf.database.swiftpass.SwiftOrderInfo;

import java.util.Map;

public class CallbackAction extends AjaxActionSupport {
    public final static String WEIXINJSPAYCALLBACK = "Callback!weixinJsPay";
    public final static String ALIJSPAYCALLBACK = "Callback!aliJsPay";
    public final static String SUCCESS = "success";
    public final static Object syncObject = new Object();

    public void weixinJsPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(SUCCESS);
    }

    public void aliJsPay() throws Exception {
        handlerCallback();
        getResponse().getWriter().write(SUCCESS);
    }

    private void handlerCallback() throws Exception {
        Map<String,Object> responseResult = XMLParser.convertMapFromXml(getInputStreamAsString());
        if (responseResult.get("result_code").toString().compareTo("0") == 0 &&
                responseResult.get("pay_result").toString().compareTo("0") == 0) {
            synchronized (syncObject) {
                String tradeNo = StringUtils.convertNullableString(responseResult.get("out_trade_no"));
                if (SwiftOrderInfo.getOrderInfoByOrderNo(tradeNo) == null) {
                    SwiftOrderInfo swiftOrderInfo = new SwiftOrderInfo();
                    swiftOrderInfo.setMchId(Long.parseLong(responseResult.get("attach").toString()));
                    swiftOrderInfo.setOutTradeNo(tradeNo);
                    swiftOrderInfo.setTimeEnd(responseResult.get("time_end").toString());
                    swiftOrderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
                    SwiftOrderInfo.insertOrderInfo(swiftOrderInfo);
                }
            }
            return;
        }

        ProjectLogger.error("Swiftpass Callback Error!");
    }
}
