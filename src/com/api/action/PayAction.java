package com.api.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.XMLParser;
import com.weixin.utils.Signature;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class PayAction extends AjaxActionSupport {
    private final static String WeixinMode = "weixin";

    private final static String WeixinMicroPay = "WeixinMicroPay";
    private final static String WeixinScanPay = "WeixinScanPay";
    private final static String WeixinPrePay = "WeixinPrePay";
    private final static String WeixinBrandWCPay = "WeixinBrandWCPay";

    public String microPay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        String mode = resquestBuffer.get("mode").toString();
        resquestBuffer.remove("mode");
        switch (mode) {
            case WeixinMode:
            default: {
                setParameter(resquestBuffer);
                return WeixinMicroPay;
            }
        }
    }

    public String scanPay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        String mode = resquestBuffer.get("mode").toString();
        resquestBuffer.remove("mode");
        switch (mode) {
            case WeixinMode:
                return WeixinScanPay;
            default:
                return WeixinScanPay;
        }
    }

    public String prePay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        String mode = resquestBuffer.get("mode").toString();
        resquestBuffer.remove("mode");
        switch (mode) {
            case WeixinMode:
                return WeixinPrePay;
            default:
                return WeixinPrePay;
        }
    }

    public String brandWCPay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        String mode = resquestBuffer.get("mode").toString();
        resquestBuffer.remove("mode");
        switch (mode) {
            case WeixinMode:
                return WeixinBrandWCPay;
            default:
                return WeixinBrandWCPay;
        }
    }

    private Map<String,Object> getRequestBuffer() throws IOException, ParserConfigurationException, IOException, SAXException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer = null;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }

        String resquestString = stringBuilder.toString();
        Map<String,Object> responseResult = XMLParser.convertMapFromXML(resquestString);
        if (!Signature.checkResponseSignValid(resquestString, responseResult.get("sub_mch_id").toString())) {
            System.out.println("CheckResponseSignValid Failed!");
        }

        if (responseResult.get("mode") == null) {
            responseResult.put("mode", WeixinMode);
        }
        return responseResult;
    }
}
