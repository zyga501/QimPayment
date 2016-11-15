package pf.swiftpass.api.RequestBean;

import pf.ProjectSettings;
import framework.utils.IdWorker;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class WeixinJsPayRequestData extends RequestData {
    public WeixinJsPayRequestData() throws UnknownHostException {
        service = "pay.weixin.jspay";
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
    public String sub_openid; // 微信用户关注商家公众号的openid
    public int total_fee; // 总金额，以分为单位，不允许包含任何字、符号
    public String mch_create_ip; // 订单生成的机器IP
    public String notify_url; // 接收中信银行通知的URL

    // option
    public String sign_agentno; // 如果不为空，则用授权渠道的密钥进行签名
    public String is_raw;
    public String callback_url; // 交易完成后跳转的URL
    public String time_start; // 订单生成时间
    public String time_expire; // 订单失效时间
    public String goods_tag; // 商品标记
    public String limit_credit_pay; // 限定用户使用微信支付时能否使用信用卡
}
