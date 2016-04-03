package com.weixin.api;

import com.weixin.api.RequestData.MicroPayRequestData;
import com.weixin.api.RequestData.OrderQueryData;
import com.weixin.database.OrderInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
    protected boolean handlerResponse(Map<String,Object> responseResult, String appsecret) throws IllegalAccessException, IOException,ParserConfigurationException, SAXException {
        try {
            String returnCode = responseResult.get("return_code").toString().toUpperCase();
            String resultCode = responseResult.get("result_code").toString().toUpperCase();
            MicroPayRequestData microPayRequestData = (MicroPayRequestData)requestData_;
            if (returnCode.compareTo("SUCCESS") == 0) {
                if (resultCode.compareTo("SUCCESS") == 0) {
                    saveOrderToDb(responseResult);
                }
                else {
                    String errorCode = responseResult.get("err_code").toString().toUpperCase();
                    switch (errorCode) {
                        case "USERPAYING" : {
                            Thread.sleep(10000);
                        }
                        case "SYSTEMERROR" : {
                            OrderQueryData orderQueryData = new OrderQueryData();
                            orderQueryData.appid = requestData_.appid;
                            orderQueryData.mch_id = requestData_.mch_id;
                            orderQueryData.sub_mch_id = requestData_.sub_mch_id;
                            orderQueryData.out_trade_no = microPayRequestData.out_trade_no;
                            OrderQuery orderQuery = new OrderQuery(orderQueryData);
                            if (!orderQuery.execute(appsecret)) {
                                return false;
                            }
                            saveOrderToDb(orderQuery.getResponseResult());
                            return true;
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }

    private boolean saveOrderToDb(Map<String,Object> responseResult) {
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

    private long createUser_;
}
