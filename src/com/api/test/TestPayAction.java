package com.api.test;

import com.api.test.RequestData.JsPayData;
import com.api.test.RequestData.MicroPayRequestData;
import com.api.test.RequestData.ScanPayRequestData;
import com.framework.action.AjaxActionSupport;
import com.framework.base.Readconfig;
import com.framework.utils.HttpUtils;
import com.framework.utils.MD5;
import com.framework.utils.XMLParser;
import com.jdpay.action.CallbackAction;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weixin.utils.Signature;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.impl.StrutsActionProxy;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestPayAction extends AjaxActionSupport {
    public void microPay() throws Exception {
        MicroPayRequestData microPayRequestData = new MicroPayRequestData();
        microPayRequestData.id = getParameter("id").toString();
        microPayRequestData.body = getParameter("body").toString();
        microPayRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
        microPayRequestData.auth_code = getParameter("auth_code").toString();
        microPayRequestData.mode = getParameter("mode").toString();
        microPayRequestData.sign = Signature.generateSign(microPayRequestData, microPayRequestData.id);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(microPayRequestData);
        HttpPost httpPost = new HttpPost(getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "MicroPay");
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse)->
            {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
        finally {
            httpPost.abort();
        }
    }

    public String scanPay() throws Exception {
        ScanPayRequestData scanPayRequestData = new ScanPayRequestData();
        scanPayRequestData.id = getParameter("id").toString();
        scanPayRequestData.body = getParameter("body").toString();
        scanPayRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
        scanPayRequestData.product_id = getParameter("product_id").toString();
        scanPayRequestData.out_trade_no = getParameter("out_trade_no").toString();
        scanPayRequestData.mode = getParameter("mode").toString();
        scanPayRequestData.redirect_uri = getParameter("redirect_uri").toString();
        scanPayRequestData.sign = Signature.generateSign(scanPayRequestData, scanPayRequestData.id);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(scanPayRequestData);
        HttpPost httpPost = new HttpPost(getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "ScanPay");
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        String responseString = new String();
        try {
            responseString = HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse)->
            {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            });
        }
            finally {
            httpPost.abort();
        }

        responseString = responseString.substring(1);
        responseString = responseString.substring(0, responseString.length() - 1);
        responseString = responseString.replace("\\", "");
        JSONObject jsonParse = JSONObject.fromObject(responseString);
        Map<String, String> map = new HashMap<>();
        map.put("code_url", jsonParse.get("code_url").toString());
        return AjaxActionComplete(map);
    }

    public void jsPay() throws Exception {
        JsPayData jsPayData = new JsPayData();
        jsPayData.id = getParameter("id").toString();
        jsPayData.body = getParameter("body").toString();
        jsPayData.total_fee = getParameter("total_fee").toString();
        jsPayData.redirect_uri = "";
        jsPayData.sign = Signature.generateSign(jsPayData, jsPayData.id);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(jsPayData);
        HttpPost httpPost = new HttpPost(getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "JsPay");
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        try {
            HttpUtils.PostRequest(httpPost, (HttpResponse httpResponse)->
            {
                Header[] headers = httpResponse.getHeaders("Location");
                if (headers.length != 0)
                    getResponse().sendRedirect(headers[0].getValue());
                return null;
            });
        }
        finally {
            httpPost.abort();
        }
    }

    public String brandWCPay() {
        return AjaxActionComplete();
    }

    public static void main(String[] args) throws Exception {
       /* String s = "eyJhbW91bnQiOiIwLjAxMDAiLCJkZXNjIjoi5pSv5LuY5oiQ5YqfIiwibWVyY2hhbnRfbm8iOiIx"+
                    "MTAyMDUwNjAwMDIiLCJvcmRlcl9ubyI6IkUxNjA1MzEwOTUwMjIiLCJwYXlfdGltZSI6IjIwMTYtMDUtMzEgMDk6NTE6NTYiLCJ"+
                    "wcm9tb3Rpb25BbW91bnQiOiIwIiwic3RhdHVzIjoiMCIsInN1Yl9tZXIiOiLkvIHnm5/np5HmioDmvJTnpLrllYbmiLcxIi"+
                    "widGVybV9ubyI6IjEiLCJ0cmFkZV9ubyI6IjIwMTYwNTMxMTEwMDExMDAzNTE2NTM3NjUiLCJ1c2VyIjoi6ZmIKioifQ==";
        String a="{\"amount\":\"0.0100\",\"desc\":\"æ\u0094¯ä»\u0098æ\u0088\u0090å\u008A\u009F\",\"merchant_no\":\"110205060002\",\"order_no\":\"E160531095022\",\"pay_time\":\"2016-05-31 09:51:56\",\"promotionAmount\":\"0\",\"status\":\"0\",\"sub_mer\":\"ä¼\u0081ç\u009B\u009Fç§\u0091æ\u008A\u0080æ¼\u0094ç¤ºå\u0095\u0086æ\u0088·1\",\"term_no\":\"1\",\"trade_no\":\"2016053111001100351653765\",\"user\":\"é\u0099\u0088**\"}";

        String bb = null;
        bb =  new String(Base64.getDecoder().decode(s));
        //bb = new String ( s.getBytes("iso-8859-1"),"utf-8");
        //bb = MD5.md5LowerCase(s,"bipy5w9rGn0rCGheaszUieiyIvFoKyUr").toLowerCase();
        //CallbackAction.saveOrderToDb(bb);
        JSONObject jsonObject = JSONObject.fromObject(bb);
        Map map =new HashMap();
        map.put("amount",jsonObject.get("amount"));
        map.put("merchant_no",jsonObject.get("merchant_no"));
        map.put("order_no",jsonObject.get("order_no"));
        map.put("pay_time",jsonObject.get("pay_time"));
        map.put("promotionAmount",jsonObject.get("promotionAmount"));
        map.put("sub_mer",jsonObject.get("sub_mer"));
        map.put("term_no",jsonObject.get("term_no"));
        map.put("trade_no",jsonObject.get("trade_no"));
        map.put("user",jsonObject.get("user"));
        System.out.print(bb);*/
       // System.out.println(Readconfig.yzfconfig("1629719000000051"));
        Map<String,Object> responseResult = XMLParser.convertMapFromXML("<xml>\n" +
                "  <appid><![CDATA[wx8888888888888888]]></appid>  \n" +
                "  <bank_type><![CDATA[CFT]]></bank_type>  \n" +
                "  <fee_type><![CDATA[CNY]]></fee_type>  \n" +
                "  <is_subscribe><![CDATA[Y]]></is_subscribe>  \n" +
                "  <mch_id><![CDATA[10012345]]></mch_id>  \n" +
                "  <nonce_str><![CDATA[60uf9sh6nmppr9azveb2bn7arhy79izk]]></nonce_str>  \n" +
                "  <openid><![CDATA[ou9dHt0L8qFLI1foP-kj5x1mDWsM]]></openid>  \n" +
                "  <out_trade_no><![CDATA[wx88888888888888881414411779]]></out_trade_no>  \n" +
                "  <result_code><![CDATA[SUCCESS]]></result_code>  \n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>  \n" +
                "  <sign><![CDATA[0C1D7F2534F1473247550A5A138F0CEB]]></sign>  \n" +
                "  <sub_mch_id><![CDATA[10012345]]></sub_mch_id>  \n" +
                "  <time_end><![CDATA[20141027200958]]></time_end>  \n" +
                "  <total_fee>1</total_fee>  \n" +
                "  <trade_type><![CDATA[JSAPI]]></trade_type>  \n" +
                "  <transaction_id><![CDATA[1002750185201410270005514026]]></transaction_id> \n" +
                "</xml>");
        JSONObject js=JSONObject.fromObject(responseResult);

        System.out.println(js.toString());
        System.out.println(MD5.md5LowerCase(js.toString(),"264550c6f4644c328d016193d61c23b8"));
        System.out.println("sss".concat("1009").concat("11uu"));
    }
}