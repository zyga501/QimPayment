package pf.jdpay.action;


import pf.database.jdpay.JdMerchantInfo;
import pf.database.merchant.SubMerchantInfo;
import pf.database.merchant.SubMerchantUser;
import pf.framework.action.AjaxActionSupport;
import pf.framework.base.ProjectSettings;
import pf.framework.utils.IdWorker;
import pf.framework.utils.StringUtils;
import pf.jdpay.api.H5Pay;
import pf.jdpay.api.MicroPay;
import pf.jdpay.api.OrderQuery;
import pf.jdpay.api.RequestBean.H5PayRequestData;
import pf.jdpay.api.RequestBean.MicroPayRequestData;
import pf.jdpay.api.RequestBean.QueryRequestData;
import pf.jdpay.api.RequestBean.TokenPayRequestData;
import pf.jdpay.api.TokenOrder;
import pf.jdpay.utils.Signature;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PayAction extends AjaxActionSupport {
    public String microPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            JdMerchantInfo merchantInfo = JdMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            MicroPayRequestData microPayRequestData = new MicroPayRequestData();
            microPayRequestData.seed =  getParameter("seed").toString();
            microPayRequestData.merchant_no = merchantInfo.getPaycodemerchantno();
            microPayRequestData.order_no =  StringUtils.convertNullableString(getParameter("orderno"),String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId()));
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "jdpay/"
                    + CallbackAction.CODEPAY;;
            microPayRequestData.notify_url = requestUrl;
            microPayRequestData.amount = Double.parseDouble(getParameter("price").toString());//0.01
            microPayRequestData.trade_name =  getParameter("goodsname").toString();
            microPayRequestData.trade_describle =  getParameter("goodsmemo").toString();
            microPayRequestData.sub_mer =  getParameter("storename").toString();
            microPayRequestData.term_no = String.valueOf(subMerchantUser.getId());
            MicroPay microPay = new MicroPay(microPayRequestData);
            if (!microPay.postRequest(merchantInfo.getPaycodemd5key())) {
                return AjaxActionComplete(microPay.getResponseResult());
            } else {
                return AjaxActionComplete(microPay.getResponseResult());
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public String tokenPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            JdMerchantInfo merchantInfo = JdMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            TokenPayRequestData tokenPayRequestData = new TokenPayRequestData();
            tokenPayRequestData.expire =  5;
            tokenPayRequestData.merchant_no = merchantInfo.getScanmerchantno();
            tokenPayRequestData.order_no =  StringUtils.convertNullableString(getParameter("orderno"),String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId()));
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "jdpay/"
                    + CallbackAction.CODEPAY;;
            tokenPayRequestData.notify_url = requestUrl;
            tokenPayRequestData.amount =Double.parseDouble(getParameter("price").toString());
            tokenPayRequestData.trade_name = StringUtils.convertNullableString(getParameter("goodsname"), subMerchantUser.getStoreName());
            if (null!=getParameter("goodsmemo"))
                tokenPayRequestData.trade_describle =   StringUtils.convertNullableString(getParameter("goodsmemo"),"描述");
            tokenPayRequestData.sub_mer = subMerchantUser.getStoreName();
            tokenPayRequestData.term_no = String.valueOf(subMerchantUser.getId());
            TokenOrder tokenOrder = new TokenOrder(tokenPayRequestData);
            if (!tokenOrder.postRequest(merchantInfo.getScanmd5key())) {
                return AjaxActionComplete(false);
            }
            else {
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("code_url",  ((Map<String,String>)(tokenOrder.getResponseResult().get("data"))).get("qrcode"));
                return AjaxActionComplete(resultMap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public String queryPay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            JdMerchantInfo merchantInfo = JdMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            QueryRequestData queryRequestData  = new QueryRequestData();
            queryRequestData.order_no = getParameter("orderno").toString();
            queryRequestData.merchant_no = merchantInfo.getPaycodemerchantno();
            OrderQuery orderQuery = new OrderQuery(queryRequestData);
            if (!orderQuery.postRequest(merchantInfo.getPaycodemd5key())) {
                QueryRequestData queryRequestData2  = new QueryRequestData();
                queryRequestData2.order_no = getParameter("orderno").toString();
                queryRequestData2.merchant_no = merchantInfo.getScanmerchantno();
                OrderQuery orderQuery2 = new OrderQuery(queryRequestData2);
                if (orderQuery2.postRequest(merchantInfo.getScanmd5key())) {
                    System.out.println(orderQuery2.getResponseResult().get("data"));
                    return AjaxActionComplete(orderQuery2.getResponseResult());
                }else
                    return AjaxActionComplete(false);
            }
            else {
                System.out.println(orderQuery.getResponseResult().get("data"));
                return AjaxActionComplete(orderQuery.getResponseResult());
            }
        } while (false);

        return AjaxActionComplete(false);
    }

    public String h5Pay() throws Exception {
        do {
            SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(getParameter("id").toString()));
            if (subMerchantUser == null) {
                break;
            }

            SubMerchantInfo subMerchantInfo = SubMerchantInfo.getSubMerchantInfoById(subMerchantUser.getSubMerchantId());
            if (subMerchantInfo == null) {
                break;
            }

            JdMerchantInfo merchantInfo = JdMerchantInfo.getMerchantInfoById(subMerchantInfo.getId());
            if (merchantInfo == null) {
                break;
            }

            H5PayRequestData h5RequestData  = new H5PayRequestData();
            h5RequestData.merchant_no = merchantInfo.getH5merchantno();
            //h5RequestData.currency = "CNY";
            h5RequestData.order_no =  StringUtils.convertNullableString(getParameter("orderno"),String.valueOf(new IdWorker(ProjectSettings.getIdWorkerSeed()).nextId()));
            String requestUrl = getRequest().getRequestURL().toString();
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/'));
            requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('/') + 1) + "jdpay/"
                    + CallbackAction.H5PAY;
            h5RequestData.notify_url = requestUrl;//"http://www.qimpay.com/qlpay/jdpay/Callback!codePay";//requestUrl;
            h5RequestData.amount =Double.parseDouble(getParameter("total_fee").toString());
            h5RequestData.note = String.valueOf(subMerchantUser.getId());
            H5Pay h5Pay = new H5Pay(h5RequestData);
            if (!h5Pay.postRequest(merchantInfo.getH5md5key())) {
                return AjaxActionComplete(false);
            } else {
                System.out.println(h5Pay.getResponseResult().toString());
                JSONObject jsonObject = JSONObject.fromObject(h5Pay.getResponseResult());
                JSONObject jsonObject2 =jsonObject.fromObject(jsonObject.get("data"));
                Map resultMap = new HashMap<>();
                resultMap.put("merchantNotifyUrl",requestUrl);
                resultMap.put("merchantNum",merchantInfo.getH5merchantno());
                resultMap.put("merchantOuterOrderNum",jsonObject2.get("order_no"));
                resultMap.put("merchantTradeAmount",String.valueOf((int)(Double.valueOf(jsonObject2.get("amount").toString())*100)));
                resultMap.put("merchantTradeNum",jsonObject2.get("order_no"));
                resultMap.put("merchantTradeTime",jsonObject2.get("trade_time"));
                resultMap.put("merchantSign", Signature.generateRSASign(resultMap,merchantInfo.getH5rsaprivatekey()));
                resultMap.put("merchantToken",(""));
                resultMap.put("merchantUserId",getParameter("id").toString());
                resultMap.put("merchantMobile","");
                resultMap.put("merchantRemark","");
                resultMap.put("merchantTradeName",subMerchantUser.getStoreName());
                resultMap.put("merchantTradeDescription","");
                resultMap.put("merchantCurrency","CNY");
                resultMap.put("data",(""));
                resultMap.put("cpTradeNum",jsonObject2.get("trade_no"));
                Map rstmap = new HashMap<>();
                rstmap.put("is_success","Y");
                rstmap.put("paystr",resultMap);
                return AjaxActionComplete(rstmap);
            }
        } while (false);

        return AjaxActionComplete(false);
    }

}