package com.framework.utils;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class HttpUtils {
    public static void main(String[] args) throws Exception {
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
        HttpGet httpGet = new HttpGet("https://github.com/VonChenPlus");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.print(response.getStatusLine().toString());
    }

    public static HttpClient Instance() { return httpClient_; }

    static {
        try {
            httpClient_ = HttpClients.custom()
                    .setConnectionManager(createrDefaultConnectionManager())
                    .setDefaultRequestConfig(createDefaultRequestConfig())
                    .build();
        }
        catch (Exception exception) {
            System.out.print(exception.getCause());
        }
    }

    private static PoolingHttpClientConnectionManager createrDefaultConnectionManager() throws UnknownHostException {
        ConnectionSocketFactory socketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", socketFactory)
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);
        HttpHost localhost = new HttpHost(InetAddress.getLocalHost());
        connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);
        return connectionManager;
    }

    private static RequestConfig createDefaultRequestConfig() {
         return RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
    }

    private static HttpClient httpClient_;
}
