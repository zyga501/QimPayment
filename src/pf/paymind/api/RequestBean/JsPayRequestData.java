package pf.paymind.api.RequestBean;

import QimCommon.utils.IdWorker;
import QimCommon.utils.MD5;
import com.sun.javafx.binding.StringFormatter;
import pf.ProjectSettings;

import java.net.InetAddress;

public class JsPayRequestData extends RequestData {
    public JsPayRequestData() throws Exception {
        trxType = "WX_SCANCODE_JSAPI";
        orderIp = InetAddress.getLocalHost().getHostAddress().toString();
        orderNum = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        encrypt = "T0";
        goodsName = "QIMENG_GOODSNAME";
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !orderIp.isEmpty()
                    && !orderNum.isEmpty()
                    && amount >= 0.01;
        }
        catch (Exception exception) {

        }

        return false;
    }

    public void buildSign(String apiKey) throws IllegalAccessException {
        sign = MD5.MD5Encode(String.format("#OnlinePay#%s#%s#%f#%s#%s#%s#%s",
                merchantNo, orderNum, amount, goodsName, orderIp, encrypt, apiKey));
    }

    public String buildRequestData() {
        return String.format("trxType=%s&merchantNo=%s&orderNum=%s&amount=%f&goodsName=%s&orderIp=%s&encrypt=%s&sign=%s",
                trxType, merchantNo, orderNum, amount, goodsName, orderIp, encrypt, sign);
    }

    public String orderIp; // 用户支付IP
    public String orderNum; // 商户订单号
    public double amount; // 金额
    public String encrypt; // T0/T1标识，若此项为T0，对应的5,11,12,13,14必填
    public String callbackUrl; // 页面回调地址
    public String serverCallbackUrl; // 服务器回调地址
}
