package pf.chanpay.api.RequestBean;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import pf.ProjectSettings;

import java.util.Map;

public class SinglePayRequestData extends RequestData {
    public SinglePayRequestData() {
        try {
            trxCode = "G10002";
            businessCode = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BUSINESS_CODE").toString();
            corpAccNo = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("CORP_ACCT_NO").toString();
            productCode = "60020002";
            accountProp = "0";
            accountType = "00";
            currency = "CNY";
        }
        catch (Exception exception) {

        }
    }

    public boolean checkParameter() {
        if (!super.checkParameter()) {
            return false;
        }

        try {
            return !businessCode.isEmpty()
                    && !corpAccNo.isEmpty()
                    && !productCode.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    protected Element buildElement() {
        Document doc = DocumentHelper.createDocument();
        Element message = doc.addElement("MESSAGE");
        message.add(super.buildElement());

        Element body = message.addElement("BODY");
        body.addElement("BUSINESS_CODE").setText(businessCode);
        body.addElement("CORP_ACCT_NO").setText(corpAccNo);
        body.addElement("PRODUCT_CODE").setText(productCode);
        body.addElement("ACCOUNT_PROP").setText(accountProp);
        if (subMertid != null) {
            body.addElement("SUB_MERCHANT_ID").setText(subMertid);
        }
        body.addElement("BANK_GENERAL_NAME").setText(bankGeneralName);
        if (accountType != null) {
            body.addElement("ACCOUNT_TYPE").setText(accountType);
        }
        body.addElement("ACCOUNT_NO").setText(accountNo);
        body.addElement("ACCOUNT_NAME").setText(accountName);
        if (province != null) {
            body.addElement("PROVINCE").setText(province);
        }
        if (city != null) {
            body.addElement("CITY").setText(city);
        }
        body.addElement("BANK_NAME").setText(bankName);
        if (bankCode != null) {
            body.addElement("BANK_CODE").setText(bankCode);
        }
        if (drctBankCode != null) {
            body.addElement("DRCT_BANK_CODE").setText(drctBankCode);
        }
        if (protocolNo != null) {
            body.addElement("PROTOCOL_NO").setText(protocolNo);
        }
        if (currency != null) {
            body.addElement("CURRENCY").setText(currency);
        }
        body.addElement("AMOUNT").setText((String.valueOf(amount)));
        if (idType != null) {
            body.addElement("ID_TYPE").setText(idType);
        }
        if (id != null) {
            body.addElement("ID").setText(id);
        }
        if (tel != null) {
            body.addElement("TEL").setText(tel);
        }
        if (corpFlowNo != null) {
            body.addElement("CORP_FLOW_NO").setText(corpFlowNo);
        }
        if (summary != null) {
            body.addElement("SUMMARY").setText(summary);
        }
        if (postscript != null) {
            body.addElement("POSTSCRIPT").setText(postscript);
        }
        return doc.getRootElement();
    }

    public String businessCode; // 业务代码, 接入生产前，业务人员会提供
    public String productCode; // 产品编码, 接入生产前，业务人员会提供
    public String corpAccNo; // 企业账号
    public String accountProp; // 对公对私
    public String bankGeneralName; // 银行通用名称
    public String accountType; // 账号类型
    public String accountNo; // 账号
    public String accountName; // 账户名称
    public String province; // 开户行所在省 */
    public String city; // 开户行所在市 */
    public String bankName; // 开户行名称 如：中国建设银行广州东山广场分理处
    public String bankCode; // 开户行号， 对方账号对应的开户行支行行号
    public String drctBankCode; // 清算行号
    public String protocolNo; // 协议号
    public String currency; // 货币类型, 人民币
    public long amount; // 金额
    public String idType; // 开户证件类型
    public String id; // 证件号
    public String tel; // 手机号
    public String corpFlowNo; // 外部企业流水号
    public String summary; // 备注
    public String postscript; // 用途
}
