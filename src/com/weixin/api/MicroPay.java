package com.weixin.api;

import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.database.OrderInfo;

import java.util.Map;

public class MicroPay extends WeixinAPI {
    public final static String MICROPAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

    public MicroPay(MicroPayRequestData microPayRequestData, long createUser) {
        requestData_ = microPayRequestData;
        createUser_ = createUser;
    }

    @Override
    protected String getAPIUri() {
        return MICROPAY_API;
    }

    @Override
    protected boolean handlerResponse(Map<String,Object> responseResult) {
        try {
            if (responseResult.get("return_code").toString().toUpperCase().compareTo("SUCCESS") == 0
                    && responseResult.get("result_code").toString().toUpperCase().compareTo("SUCCESS") == 0) {
                MicroPayRequestData microPayRequestData = (MicroPayRequestData)requestData_;
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setAppid(responseResult.get("appid").toString());
                orderInfo.setMchId(responseResult.get("mch_id").toString());
                orderInfo.setSubMchId(responseResult.get("sub_mch_id").toString());
                orderInfo.setBody(responseResult.get("attach").toString());
                orderInfo.setTransactionId(responseResult.get("transaction_id").toString());
                orderInfo.setOutTradeNo(responseResult.get("out_trade_no").toString());
                orderInfo.setBankType(responseResult.get("bank_type").toString());
                orderInfo.setTotalFee(Integer.parseInt(responseResult.get("total_fee").toString()));
                orderInfo.setTimeEnd(responseResult.get("time_end").toString());
                orderInfo.setCreateUser(createUser_);
                return OrderInfo.insertOrderInfo(orderInfo);
            }
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }

    private long createUser_;
}
