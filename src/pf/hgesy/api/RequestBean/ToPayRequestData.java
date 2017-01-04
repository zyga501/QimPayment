package pf.hgesy.api.RequestBean;

import QimCommon.utils.IdWorker;
import QimCommon.utils.StringUtils;
import pf.ProjectSettings;

public class ToPayRequestData extends RequestData {
    public ToPayRequestData() {
        order_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        request_time = StringUtils.generateDate("yyyyMMddHHmmss", "GMT+8");
        auth_code = "";
    }

    @Override
    public String buildRequestData() {
        return String.format("account=%s&auth_code=%s&order_no=%s&product_code=%s&request_time=%s&sign=%s&total_fee=%s",
                account, auth_code, order_no, product_code, request_time, sign, total_fee);
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !account.isEmpty()
                    && !request_time.isEmpty()
                    && !order_no.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String account; // 商户账号
    public String auth_code; // 付款码
    public String order_no; // 商户订单号
    public String product_code; // 产品代码
    public String request_time; // 请求时间
    public String total_fee; // 交易金额

    // option
    public String subject;
}
