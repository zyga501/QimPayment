package com.framework.action;


import com.opensymphony.xwork2.ActionContext;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class WebAction extends AjaxActionSupport {
    public static String judgeUserAgent() throws IOException {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String userAgent = request.getHeader("User-Agent");
        System.out.println("Your Browser User-Agent is:"+userAgent);
        return userAgent;
    }

    public String choogpay() throws IOException {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        System.out.println("Your Browser User-Agent is:"+userAgent);
        if (userAgent.contains("micromessenger")){
            return "WeixinJsPay";
        }
        else if (userAgent.contains("walletclient")){
            return "JDJsPay";
        }
        else
            return "";
    }
}
