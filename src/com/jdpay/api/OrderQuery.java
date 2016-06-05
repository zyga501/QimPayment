package com.jdpay.api;

import com.jdpay.api.RequestData.MicroPayRequestData;
import com.jdpay.api.RequestData.QueryRequestData;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

///用户展示条码，支付///
public class OrderQuery extends JDAPIWithSign {
    public final static String ORDERQUERY_API = "https://payscc.jdpay.com/order/query";

    public OrderQuery(QueryRequestData queryRequestData) {
        requestData_ = queryRequestData;
    }

    @Override
    protected String getAPIUri() {
        return ORDERQUERY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        System.out.print("ORDERQUERY_API handlerResponse");
        return responseResult.get("is_success").equals("Y");
    }

    private long createUser_;
}