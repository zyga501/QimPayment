package pf.chanpay.api.RequestBean;

import pf.framework.base.ProjectSettings;
import pf.framework.utils.IdWorker;
import pf.framework.utils.StringUtils;

import java.util.Map;

public class RequestData {
    public RequestData() {
        try {
            timestamp = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
            mertid = ((Map<Object, Object>)ProjectSettings.getData("chanPay")).get("MERCHANT_ID").toString();
            reqSn = mertid + timestamp + String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed() % 100000).nextId());
        }
        catch (Exception exception) {

        }
    }

    public boolean checkParameter() {
        try {
            return !mertid.isEmpty()
                    && !reqSn.isEmpty()
                    && !trxCode.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String trxCode; // 报文交易代码 标识此请求为“单笔垫资付款业务”
    public String mertid; // 商户代码 标识商户的唯一ID，15位
    public String reqSn; // 交易请求号 数据格式：(15位)商户号 + (12位)yyMMddHHmmss时间戳 + (5位)循环递增序号 = (32位)唯一交易号；
    public String timestamp; // 受理时间 代支付系统接收到交易请求时服务器时间； 对于交易的发起时间以此时间为准
    public String signed_msg; // 签名信息

    // option
    public String subMertid; // 二级商户号
}
