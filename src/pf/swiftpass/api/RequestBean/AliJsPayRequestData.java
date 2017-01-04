package pf.swiftpass.api.RequestBean;

import pf.ProjectSettings;
import QimCommon.utils.IdWorker;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AliJsPayRequestData extends RequestData {
    public AliJsPayRequestData() throws UnknownHostException {
        service = "pay.alipay.jspay";
        mch_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !body.isEmpty()
                    && total_fee > 0
                    && !notify_url.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String out_trade_no; // 商户系统内部的订单号
    public String body; // 商品描述
    public int total_fee; // 总金额，以分为单位，不允许包含任何字、符号
    public String mch_create_ip; // 订单生成的机器IP
    public String notify_url; // 接收中信银行通知的URL

    // option
    public String time_start; // 订单生成时间
    public String time_expire; // 订单失效时间
    public String op_user_id; // 操作员帐号,默认为商户号
    public String goods_tag; // 商品标记
    public String product_id; // 预留字段此id 为静态可打印的二维码中包含的商品ID，商户自行维护。
    public String buyer_logon_id; // 买家支付宝账号,和buyer_ id 不能同时为空
    public String buyer_id; // 买家的支付宝用户ID,和buyer_logon_id不能同时为空
}
