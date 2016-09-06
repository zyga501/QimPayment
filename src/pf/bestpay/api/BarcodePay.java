package pf.bestpay.api;

import pf.bestpay.api.RequestBean.BarcodePayRequestData;
import pf.framework.utils.ClassUtils;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class BarcodePay extends BestPayWithSign {
    public final static String BARCODEPAY_API = "https://webpaywg.bestpay.com.cn/barcode/placeOrder";

    public BarcodePay(BarcodePayRequestData requestData) {
        requestData_ = requestData;
    }

    @Override
    protected String getAPIUri() {
        return BARCODEPAY_API;
    }

    @Override
    protected StringEntity buildPostStringEntity() throws UnsupportedEncodingException {
        return new UrlEncodedFormEntity(ClassUtils.ConvertToList(requestData_, true, false), Consts.UTF_8);
    }

    @Override
    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        if (responseResult != null && responseResult.containsKey("result")) {
            Map<String, Object> payResult = (Map<String, Object>)responseResult.get("result");
            if (payResult != null && payResult.containsKey("transStatus")) {
                return payResult.get("transStatus").toString().compareTo("B") == 0;
            }
        }

        return false;
    }

}
