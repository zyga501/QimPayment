package com.api.action;

import com.api.mode.BaseMode;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.StringUtils;
import com.framework.utils.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CommonAction extends AjaxActionSupport {
    private final static String WEIXINMODE = "weixin";

    public String MicroPay() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).microPay());
    }

    public String ScanPay() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).scanPay());
    }

    public String PrePay() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).prePay());
    }

    public String BrandWCPay() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).brandWCPay());
    }

    public String OrderQuery() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).orderQuery());
    }

    public String OrderInsert() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).orderQuery());
    }

    private void parseRequestBuffer() throws IOException, ParserConfigurationException, IOException, SAXException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();

        String resquestString = stringBuilder.toString();
        requestBuffer_ = XMLParser.convertMapFromXML(resquestString);
    }

    private BaseMode createMode(String mode) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        BaseMode baseMode;
        switch (mode) {
            case WEIXINMODE:
            default:
                baseMode = (BaseMode)Class.forName("com.api.mode.WeixinMode").newInstance();
        }
        baseMode.initMode(requestBuffer_);
        return baseMode;
    }

    private String handlerResult(String modeResult) {
        if (modeResult.isEmpty()) {
            return AjaxActionComplete();
        }

        setParameter(requestBuffer_);
        return modeResult;
    }

    private Map<String,Object> requestBuffer_ = new HashMap<>();
}
