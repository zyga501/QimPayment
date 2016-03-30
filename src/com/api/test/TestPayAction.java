package com.api.test;

import com.api.test.RequestData.MicroPayRequestData;
import com.framework.action.AjaxActionSupport;
import com.framework.utils.HttpUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weixin.utils.Signature;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class TestPayAction extends AjaxActionSupport {
    public void microPay() throws UnknownHostException, IllegalAccessException {
            MicroPayRequestData microPayRequestData = new MicroPayRequestData();
            microPayRequestData.sub_mch_id = getParameter("sub_mch_id").toString();
            microPayRequestData.body = getParameter("body").toString();
            microPayRequestData.total_fee = Integer.parseInt(getParameter("total_fee").toString());
            microPayRequestData.auth_code = getParameter("auth_code").toString();
            microPayRequestData.mode = getParameter("mode").toString();
            microPayRequestData.sign = Signature.generateSign(microPayRequestData, microPayRequestData.sub_mch_id);
            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            String postDataXML = xStreamForRequestPostData.toXML(microPayRequestData);
            HttpPost httpPost = new HttpPost(getRequest().getRequestURL().substring(0, getRequest().getRequestURL().lastIndexOf("/") + 1) + "Pay!microPay");
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);

            String responseString = new String();
            try {
                CloseableHttpClient httpClient = HttpUtils.Instance();
                httpClient.execute(httpPost);
            }
            catch (ConnectionPoolTimeoutException e) {
            }
            catch (ConnectTimeoutException e) {
            }
            catch (SocketTimeoutException e) {
            }
            catch (Exception e) {
            }
            finally {
                httpPost.abort();
            }
    }

    public String scanPay() {
        return AjaxActionComplete();
    }

    public String prePay() {
        return AjaxActionComplete();
    }

    public String brandWCPay() {
        return AjaxActionComplete();
    }
}