package pf.chanpay.api.RequestBean;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import pf.chanpay.utils.Signature;
import pf.ProjectSettings;
import framework.utils.IdWorker;
import framework.utils.Logger;
import framework.utils.StringUtils;

import java.io.StringWriter;
import java.util.Map;

public abstract class RequestData {
    public RequestData() {
        try {
            timestamp = StringUtils.generateDate("yyyyMMddhhmmss", "GMT+8");
            mertid = ((Map<Object, Object>)ProjectSettings.getData("chanPay")).get("MERCHANT_ID").toString();
            reqSn = mertid + timestamp.substring(2) + String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId() % 100000);
            version = "01";
        }
        catch (Exception exception) {

        }
    }

    public boolean checkParameter() {
        try {
            return !mertid.isEmpty()
                    && !reqSn.isEmpty()
                    && !trxCode.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String generateRequestData() {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setIndent(false);
            format.setNewlines(false);
            format.setNewLineAfterDeclaration(false);
            StringWriter stringWriter = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(stringWriter, format);
            xmlWriter.write(buildElement());
            xmlWriter.close();
            String requestXml = stringWriter.toString();

            String sign = Signature.generateSign(requestXml);
            int beginIndex = requestXml.indexOf("</SIGNED_MSG>");
            beginIndex = beginIndex < 0 ? 0 : beginIndex;
            StringBuilder out = new StringBuilder();
            out.append(requestXml, 0, beginIndex);
            out.append(sign);
            out.append(requestXml, beginIndex, requestXml.length());
            return out.toString();
        }
        catch (Exception exception) {
            Logger.debug("ChanPay GenerateRequestData Error!");
        }

        return "";
    }

    protected Element buildElement() {
        try {
            Element info = DocumentHelper.createElement("INFO");
            info.addElement("TRX_CODE").setText(trxCode);
            info.addElement("VERSION").setText(version);
            info.addElement("MERCHANT_ID").setText(mertid);
            info.addElement("REQ_SN").setText(reqSn);
            info.addElement("TIMESTAMP").setText(timestamp);
            info.addElement("SIGNED_MSG").setText("");
            return info;
        }
        catch (Exception exception) {

        }

        return null;
    }

    public String trxCode; // 报文交易代码 标识此请求为“单笔垫资付款业务”
    public String mertid; // 商户代码 标识商户的唯一ID，15位
    public String reqSn; // 交易请求号 数据格式：(15位)商户号 + (12位)yyMMddHHmmss时间戳 + (5位)循环递增序号 = (32位)唯一交易号；
    public String timestamp; // 受理时间 代支付系统接收到交易请求时服务器时间； 对于交易的发起时间以此时间为准
    public String version;

    // option
    public String subMertid; // 二级商户号
}
