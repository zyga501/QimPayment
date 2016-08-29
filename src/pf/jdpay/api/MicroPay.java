package pf.jdpay.api;

import pf.jdpay.api.RequestData.MicroPayRequestData;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
///用户展示条码，支付///
public class MicroPay extends JDAPIWithSign {
    public final static String MICROPAY_API = "https://pcplatform.jdpay.com/api/pay";

    public MicroPay(MicroPayRequestData microPayRequestData) {
        requestData_ = microPayRequestData;
    }

    @Override
    protected String getAPIUri() {
        return MICROPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        System.out.print("MicroPay handlerResponse");
        return responseResult.containsKey("is_success") && (responseResult.get("is_success").toString().equals("Y"));
//        return false;
    }

    private long createUser_;
}
