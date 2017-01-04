package pf.swiftpass.api.RequestBean;

import QimCommon.utils.IdWorker;
import pf.ProjectSettings;

import java.net.InetAddress;

public class AliNativeRequestData extends RequestData {
    public AliNativeRequestData() throws Exception {
        service = "pay.alipay.nativev2";
        mch_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
    }

    public String out_trade_no; // 商户系统内部的订单号
    public String body; // 商品描述
    public int total_fee; // 总金额，以分为单位，不允许包含任何字、符号
    public String mch_create_ip; // 订单生成的机器IP
    public String notify_url; // 接收中信银行通知的URL

    // option
    String product_id;
}
