package pf.paymind.api.RequestBean;

import QimCommon.utils.IdWorker;
import pf.ProjectSettings;

import java.net.InetAddress;

public class JsPayRequestData extends RequestData {
    public JsPayRequestData() throws Exception {
        trxType = "WX_SCANCODE_JSAPI";
        orderIp = InetAddress.getLocalHost().getHostAddress().toString();
        orderNum = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
    }

    public String orderNum; // 商户订单号
    public double amount; // 金额
    public String callbackUrl; // 页面回调地址
    public String serverCallbackUrl; // 服务器回调地址
}
