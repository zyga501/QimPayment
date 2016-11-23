package pf.api.test;

import pf.api.test.RequestData.JsPayData;
import pf.api.test.RequestData.MicroPayRequestData;
import pf.api.test.RequestData.ScanPayRequestData;
import QimCommon.struts.AjaxActionSupport;
import QimCommon.utils.HttpUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import pf.weixin.utils.Signature;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

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
        jsPayData.mode = "SwiftPass";
        jsPayData.id = getParameter("id").toString();
        jsPayData.body = getParameter("body").toString();
        jsPayData.total_fee = getParameter("total_fee").toString();
        jsPayData.method = "alipay.jspay";
        jsPayData.sign = Signature.generateSign(jsPayData, jsPayData.id);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(jsPayData);
        HttpPost httpPost = new HttpPost("http://www.jeanhk.top/QimPayment/api/JsPay");
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
}