package pf.chanpay.api.RequestBean;

import pf.framework.base.ProjectSettings;

import java.util.Map;

public class OneTimeRequestData extends RequestData {
    public OneTimeRequestData() {
        try {
            trxCode = "G10001";
            businessCode = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BUSINESS_CODE").toString();
            corpAccNo = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("CORP_ACCT_NO").toString();
            productCode = "60020002";
            accountProp = "1";
            bankGeneralName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BANK_GENERAL_NAME").toString();
            accountNo = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("ACCOUNT_NO").toString();
            accountName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("ACCOUNT_NAME").toString();
            bankName = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("BANK_NAME").toString();
        }
        catch (Exception exception) {

        }
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
