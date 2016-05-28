package com.jdpay.action;

import com.framework.action.AjaxActionSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CallbackAction extends AjaxActionSupport {
    public final static String CODEPAY = "Callback!codePay";

    public String codePay() throws Exception {
        handlerCallback();
        return AjaxActionComplete();
    }

    private boolean handlerCallback() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getRequest().getInputStream(), "utf-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String lineBuffer;
        while ((lineBuffer = bufferedReader.readLine()) != null) {
            stringBuilder.append(lineBuffer);
        }
        bufferedReader.close();
        return false;
    }
}
