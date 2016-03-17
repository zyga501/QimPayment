package com.framework.utils;

public class StringUtils {
    public static String convertNullableString(Object object) {
        if (object == null) {
            return "";
        }

        return object.toString();
    }

    public static String convertNullableString(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        return object.toString();
    }
}
