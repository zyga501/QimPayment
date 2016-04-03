package com.api.action;

import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
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
        switch (StringUtils.convertNullableString(resquestBuffer.get("mode"))) {
            case WeixinMode:
            default: {
                if (!Signature.checkSignValid(resquestBuffer, resquestBuffer.get("id").toString())) {
                    System.out.println("checkSignValid Failed!");
                    return AjaxActionComplete();
                }
                setParameter(resquestBuffer);
                return WeixinMicroPay;
            }
        }
    }

    public String scanPay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        switch (StringUtils.convertNullableString(resquestBuffer.get("mode"))) {
            case WeixinMode:
            default: {
                if (!Signature.checkSignValid(resquestBuffer, resquestBuffer.get("id").toString())) {
                    System.out.println("checkSignValid Failed!");
                    return AjaxActionComplete();
                }
                setParameter(resquestBuffer);
                return WeixinScanPay;
            }
        }
    }

    public String prePay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        switch (StringUtils.convertNullableString(resquestBuffer.get("mode"))) {
            case WeixinMode:
            default: {
                if (!Signature.checkSignValid(resquestBuffer, resquestBuffer.get("id").toString())) {
                    System.out.println("checkSignValid Failed!");
                    return AjaxActionComplete();
                }
                setParameter(resquestBuffer);
                return WeixinPrePay;
            }
        }
    }

    public String brandWCPay() throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> resquestBuffer = getRequestBuffer();
        switch (StringUtils.convertNullableString(resquestBuffer.get("mode"))) {
            case WeixinMode:
            default: {
                if (!Signature.checkSignValid(resquestBuffer, resquestBuffer.get("id").toString())) {
                    System.out.println("checkSignValid Failed!");
                    return AjaxActionComplete();
                }
                setParameter(resquestBuffer);
                return WeixinBrandWCPay;
            }
        }
    }

    private Map<String,Object> getRequestBuffer() throws IOException, ParserConfigurationException, IOException, SAXException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer = null;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }

        String resquestString = stringBuilder.toString();
        return XMLParser.convertMapFromXML(resquestString);
    }
}
