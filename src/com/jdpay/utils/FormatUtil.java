package com.jdpay.utils;

public class FormatUtil {
    public static String stringBlank(String value) {
        if (value == null || value.equals("")) {
            value = "";
        }
        return value.replaceAll("\r|\n", "");
    }

}