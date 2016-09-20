package pf.action;


import com.opensymphony.xwork2.ActionContext;
import framework.action.AjaxActionSupport;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WebAction extends AjaxActionSupport {
    public  String judgeUserAgent() throws IOException {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String userAgent = request.getHeader("User-Agent");
        System.out.println("Your Browser User-Agent is:"+userAgent);
        return userAgent;
    }

    public String ChoogPayment() throws IOException {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        System.out.println("Your Browser User-Agent is:"+userAgent);
        if (userAgent.contains("micromessenger")){
            return "WeixinJsPay";
        }
        else if (userAgent.contains("walletclient")){
            return "JDJsPay";
        }
        else if (userAgent.contains("alipayclient")){
            return "AliJsPay";
        }
        else if (userAgent.contains("bestpay")){
            return "BestJsPay";
        }
        else
            return "goto404";
    }
}
