package pf.hgesy.api.RequestBean;

import QimCommon.utils.IdWorker;
import QimCommon.utils.StringUtils;
import pf.ProjectSettings;

public class DirectPayRequestData extends RequestData {
    public DirectPayRequestData() {
        order_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
        request_time = StringUtils.generateDate("yyyyMMddHHmmss", "GMT+8");
        product_code = "C3T1";
    }

    @Override
    public String buildRequestData() {
        return String.format("account=%s&order_no=%s&product_code=%s&request_time=%s&sign=%s&total_fee=%s",
                account, order_no, product_code, request_time, sign, total_fee);
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

    public String account;
    public String order_no;
    public String product_code;
    public String request_time;
    public String subject;
    public String total_fee;
}
