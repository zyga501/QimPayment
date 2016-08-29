package pf.api.action;

import pf.api.mode.BaseMode;
import pf.framework.action.AjaxActionSupport;
import pf.framework.utils.StringUtils;
import pf.framework.utils.XMLParser;
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

    public String JsPay() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).jsPay());
    }

    public String OrderQuery() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).orderQuery());
    }

    public String OrderInsert() throws Exception {
        parseRequestBuffer();
        String mode = StringUtils.convertNullableString(requestBuffer_.get("mode"));
        return handlerResult(createMode(mode).orderInsert());
    }

    private void parseRequestBuffer() throws IOException, ParserConfigurationException, IOException, SAXException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();
        requestBuffer_ = XMLParser.convertMapFromXML(stringBuilder.toString());
    }

    private BaseMode createMode(String mode) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        BaseMode baseMode;
        switch (mode) {
            case WEIXINMODE:
            default:
                baseMode = (BaseMode)Class.forName("WeixinMode").newInstance();
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
