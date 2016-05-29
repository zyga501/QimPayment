package com.jdpay.action;


import com.database.jdpay.MerchantInfo;
import com.database.merchant.SubMerchantInfo;
import com.database.merchant.SubMerchantUser;
import com.framework.action.AjaxActionSupport;
import com.framework.base.ProjectSettings;
import com.framework.utils.IdWorker;
import com.framework.utils.Logger;
import com.jdpay.api.MicroPay;
import com.jdpay.api.RequestData.MicroPayRequestData;
import com.jdpay.api.RequestData.TokenPayRequestData;
import com.jdpay.api.TokenOrder;

public class PayAction extends AjaxActionSupport {

    public static void main(String[] args) throws Exception {
        /**
         * 商户号
         */
        String merchant_no = "110205060002";//"110123395001";
        String md5Key ="bipy5w9rGn0rCGheaszUieiyIvFoKyUr";// "ohqppqKpOyJrFuagiAbsQnlfluisZUvo";bipy5w9rGn0rCGheaszUieiyIvFoKyUr
        /**
         * 商户订单号 每次请求支付必须不相同。
         */
        String order_no = "201605251453123452";
        /**
         * 用户付款码二维码内容
         */
        //String seed = "185778393910356286";
        long expire_ = 5;
        /**
         * 扣款成功通知回调地址
         */
        String notify_url = "http://www.jd.com";
        /**
         * 测试商户号单笔风控限制不超过10元
         */
        double amount = 0.01;

        String trade_name = "测试交易"; // 必传递
        String trade_describle = "测试交易交易描述";

        String sub_mer = "1";
        String term_no = "2";

        TokenPayRequestData tokenPayRequestData = new TokenPayRequestData();
        tokenPayRequestData.order_no = order_no;
        tokenPayRequestData.expire = expire_;
        tokenPayRequestData.merchant_no = merchant_no;
        tokenPayRequestData.order_no = order_no;
        tokenPayRequestData.notify_url = notify_url;
        tokenPayRequestData.amount = amount;
        tokenPayRequestData.trade_name = trade_name;
        tokenPayRequestData.trade_name = "交易标题"; // 必传递
        tokenPayRequestData.trade_describle = "交易描述";
        tokenPayRequestData.sub_mer = "1";
        tokenPayRequestData.term_no = "2";
        TokenOrder tokenOrder = new TokenOrder(tokenPayRequestData);
        if (!tokenOrder.postRequest(md5Key)){
            Logger.warn("TokenPay EWM Failed!");
            return;
        }
        else {
            Logger.info("TokenPay EWM Succeed!");
        }
//        MicroPayRequestData microPayRequestData = new MicroPayRequestData();
//        microPayRequestData.order_no = order_no;
//        microPayRequestData.seed = seed;
//        microPayRequestData.merchant_no = merchant_no;
//        microPayRequestData.order_no = order_no;
//        microPayRequestData.notify_url = notify_url;
//        microPayRequestData.amount = amount;
//        microPayRequestData.trade_name = trade_name;
//        microPayRequestData.trade_name = "交易标题"; // 必传递
//        microPayRequestData.trade_describle = "交易描述";
//        microPayRequestData.sub_mer = "1";
//        microPayRequestData.term_no = "2";
//        MicroPay microPay =new MicroPay(microPayRequestData);
//        if (!microPay.postRequest(md5Key)){
//            Logger.warn("MicroPay Failed!");
//            return;// return AjaxActionComplete(false);
//        }
//        else {
//            Logger.info("MicroPay Succeed!");
//        }
    }

    public String microPay() throws Exception {
        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
        if (subMerchantUser != null) {
            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo != null) {
                MerchantInfo merchantInfo = MerchantInfo.getMerchantInfoById(subMerchantInfo.getMerchantId());
                if (merchantInfo != null) {
                    MicroPayRequestData microPayRequestData = new MicroPayRequestData();
                    microPayRequestData.order_no = getParameter("orderno").toString();
                    microPayRequestData.seed =  getParameter("seed").toString();
                    microPayRequestData.merchant_no = merchantInfo.getPaycodemerchantno();
                    microPayRequestData.order_no = String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId());
                    String requestUrl = getRequest().getRequestURL().toString();
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
                    requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "weixin/"
                            + CallbackAction.CODEPAY;;
                    microPayRequestData.notify_url = requestUrl;
                    microPayRequestData.amount = Float.parseFloat(getParameter("price").toString());//0.01
                    microPayRequestData.trade_name =  getParameter("goodsname").toString();
                    microPayRequestData.trade_describle =  getParameter("goodsmemo").toString();
                    microPayRequestData.sub_mer = "1";
                    microPayRequestData.term_no = "2";
                    MicroPay microPay = new MicroPay(microPayRequestData);
                    if (!microPay.postRequest(merchantInfo.getPaycodemd5key())) {
                        Logger.warn("MicroPay Failed!");
                        return AjaxActionComplete(false);
                    } else {
                        Logger.info("MicroPay Succeed!");
                        return AjaxActionComplete(microPay.getResponseResult());
                    }
                }
            }
        }
        return AjaxActionComplete(false);
    }

    public String tokenPay(){

        return AjaxActionComplete(false);
    }
}
