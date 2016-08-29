package pf.weixin.api.RequestData;

import pf.framework.base.ProjectSettings;
import pf.framework.utils.IdWorker;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MicroPayRequestData extends RequestData {
    public MicroPayRequestData() throws UnknownHostException {
        spbill_create_ip = InetAddress.getLocalHost().getHostAddress().toString();
        out_trade_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !body.isEmpty()
                    && !out_trade_no.isEmpty()
                    && total_fee >= 1
                    && !spbill_create_ip.isEmpty()
                    && !auth_code.isEmpty();
        }
        catch (Exception exception) {
            return false;
        }
    }

    public String body; // 商品描述 商品或支付单简要描述
    public String out_trade_no; // 商户订单号 商户系统内部的订单号,32个字符内、可包含字母
    public Integer total_fee; // 总金额 订单总金额，单位为分，只能为整数
    public String spbill_create_ip; // 调用微信支付API的机器IP
    public String auth_code; // 授权码 扫码支付授权码，设备读取用户微信中的条码或者二维码信息

    // option
    public String device_info; // 设备号 终端设备号(商户自定义，如门店编号)
    public String detail; // 商品详情 商品名称明细列表
    public String attach; // 附加数据 在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    public String fee_type; // 货币类型 符合ISO 4217标准的三位字母代码，默认人民币：CNY
    public String goods_tag; // 商品标记 商品标记，代金券或立减优惠功能的参数
    public String limit_pay; // 指定支付方式 no_credit--指定不能使用信用卡支付
}
