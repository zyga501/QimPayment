package pf.bestpay.api;

import pf.bestpay.api.RequestBean.OrderPayRequestData;
import pf.framework.utils.ClassUtils;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class OrderPayAPI extends BestPayAPIWithSign {
    public final static String ORDERPAY_API = "https://webpaywg.bestpay.com.cn/order.action";

    public OrderPayAPI(OrderPayRequestData requestData) {
        requestData_ = requestData;
    }

    @Override
    protected String getAPIUri() {
        return ORDERPAY_API;
    }

    @Override
    protected StringEntity buildPostStringEntity() throws UnsupportedEncodingException {
        return new UrlEncodedFormEntity(ClassUtils.ConvertToList(requestData_, true, true), Consts.UTF_8);
    }

    @Override
    protected boolean parseResponse(String responseString) throws Exception {
        return "00&手机客户端下单成功".compareTo(responseString) == 0;
    }
}
