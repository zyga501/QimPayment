package com.alipay.api;

import com.alipay.api.RequestData.TradePayRequestData;
import com.database.alipay.AliOrderInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

import static com.message.WeixinMessage.sendAliTemplateMessage;

public class TradePay extends AliPayAPIWithSign {
    public final static String TRADEPAY_API = "https://openapi.alipay.com/gateway.do";

    public TradePay(TradePayRequestData requestData, long createUser) {
        requestData_ = requestData;
        createUser_ = createUser;
        requestData_.method = "alipay.trade.pay";
    }

    public Map<String, Object> getPayResponse() {
        return payResponse_;
    }

    @Override
    protected String getAPIUri() {
        return TRADEPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        try {
            if (!responseResult.containsKey("alipay_trade_pay_response")) {
                return false;
            }

            payResponse_ = (Map<String, Object>)responseResult.get("alipay_trade_pay_response");
            if (payResponse_.get("code").toString().compareTo("10000") == 0 && payResponse_.get("msg").toString().compareTo("Success") == 0)
            {
                TradePayRequestData requestData = (TradePayRequestData)requestData_;
                AliOrderInfo orderInfo = new AliOrderInfo();
                orderInfo.setAppid(requestData.app_id);
                orderInfo.setMchId(requestData.mchId);
                orderInfo.setSubject(requestData.subject);
                orderInfo.setTradeNo(payResponse_.get("trade_no").toString());
                orderInfo.setOutTradeNo(payResponse_.get("out_trade_no").toString());
                orderInfo.setFundChannel(((JSONObject)((JSONArray)payResponse_.get("fund_bill_list")).get(0)).getString("fund_channel"));
                orderInfo.setTotalAmount(Double.parseDouble(payResponse_.get("total_amount").toString()));
                orderInfo.setGmtPayment(payResponse_.get("gmt_payment").toString());
                orderInfo.setCreateUser(createUser_);
                orderInfo.setOpenId(payResponse_.get("open_id").toString());
                sendAliTemplateMessage(payResponse_.get("out_trade_no").toString());
                return AliOrderInfo.insertOrderInfo(orderInfo);
            }
        }
        catch (Exception exception) {
            return false;
        }

        return false;
    }

    private long createUser_;
    private Map<String, Object> payResponse_;
}
