package com.jdpay.api;

import com.framework.utils.HttpUtils;
import com.framework.utils.JsonUtils;
import com.framework.utils.Logger;
import com.framework.utils.XMLParser;
import com.jdpay.api.RequestData.RequestData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.jdpay.api.RequestData.MicroPayRequestData;
import com.jdpay.utils.Signature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JDAPIWithSign extends JDAPI {
    public Map<String, Object> getResponseResult() {
        return responseResult_;
    }

    public boolean postRequest(String apiKey) throws Exception {
        if (!requestData_.checkParameter() || apiKey.isEmpty()) {
            Logger.warn(this.getClass().getName() + " CheckParameter Failed!");
            return false;
        }

        String sign = Signature.generateSign(requestData_, apiKey_ = apiKey);
        requestData_.sign = sign;

        String apiUri = getAPIUri();
        if (apiUri.isEmpty()) {
            return false;
        }
        Logger.debug("Request Url:\r\n" + apiUri);

        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(requestData_);
        Logger.debug("Reqest Data:\r\n" + postDataXML);

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        /*NameValuePair amountPair = new BasicNameValuePair("amount", requestData_.amount + "");
        NameValuePair merchantPair = new BasicNameValuePair("merchant_no",
                requestData_.merchant_no);
        NameValuePair orderNumberPair = new BasicNameValuePair("order_no",
                requestData_.order_no);
        NameValuePair seedPair = new BasicNameValuePair("seed", requestData_.seed);
        NameValuePair notifyUrlPair = new BasicNameValuePair("notify_url",
                requestData_.notify_url);
        NameValuePair signPair = new BasicNameValuePair("sign", requestData_.sign);
        NameValuePair tradeDescriblePair = new BasicNameValuePair(
                "trade_describle", requestData_.trade_describle);
        NameValuePair tradeNamePair = new BasicNameValuePair("trade_name",
                requestData_.trade_name);
        NameValuePair subMerPair = new BasicNameValuePair("sub_mer", requestData_.sub_mer);
        NameValuePair termNoPair = new BasicNameValuePair("term_no", requestData_.term_no);
        list.add(amountPair);
        list.add(merchantPair);
        list.add(notifyUrlPair);
        list.add(seedPair);
        list.add(orderNumberPair);
        list.add(signPair);
        list.add(tradeDescriblePair);
        list.add(tradeNamePair);
        list.add(subMerPair);
        list.add(termNoPair);*/
        list = Signature.mapToNameValuePairList(requestData_);
        HttpPost httpPost = new HttpPost(apiUri);
        HttpEntity postEntity = new UrlEncodedFormEntity(list, "utf-8");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse) -> {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }

        System.out.println("Response Data:\r\n" + responseString);

        responseResult_ = JsonUtils.toMap(responseString,true);
        if (!Signature.checkSignValid(responseResult_, apiKey)) {
            Logger.warn(this.getClass().getName() + " CheckSignValid Failed!");
            return false;
        }

        return handlerResponse(responseResult_);
    }

    protected boolean handlerResponse(Map<String, Object> responseResult) throws Exception {
        return true;
    }

    protected RequestData requestData_;
    protected String apiKey_;
    protected Map<String, Object> responseResult_;
}
