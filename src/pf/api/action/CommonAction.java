package pf.api.action;

import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.StringUtils;
import QimCommon.utils.XMLParser;
import org.xml.sax.SAXException;
import pf.api.mode.BaseMode;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommonAction extends AjaxActionSupport {
    private final static String WEIXINMODE = "weixin";
    private final static String SWIFTPASSMODE = "swiftpass";

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
        requestBuffer_ = XMLParser.convertMapFromXml(getInputStreamAsString());
    }

    private BaseMode createMode(String mode) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        BaseMode baseMode;
        switch (mode.toLowerCase()) {
            case SWIFTPASSMODE:
                baseMode = (BaseMode)Class.forName("pf.api.mode.SwiftPassMode").newInstance();
                break;
            case WEIXINMODE:
            default:
                baseMode = (BaseMode)Class.forName("pf.api.mode.WeixinMode").newInstance();
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
