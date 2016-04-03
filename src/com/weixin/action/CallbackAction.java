package com.weixin.action;

import com.framework.action.AjaxActionSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CallbackAction extends AjaxActionSupport {
    public final static String SCANPAYCALLBACK = "Callback!scanPay";
    public final static String BRANDWCPAYCALLBACK = "Callback!brandWCPay";

    public void scanPay() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer = null;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }

        String resquestString = stringBuilder.toString();
        System.out.print(resquestString);
    }

    public void brandWCPay() {
        System.out.print("brandWCPay Callback\n");
    }
}
