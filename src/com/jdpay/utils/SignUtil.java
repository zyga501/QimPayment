package com.jdpay.utils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SignUtil {
    private  static List<String> signKeyList = Arrays.asList("merchantNotifyUrl", "merchantNum","merchantOuterOrderNum", "merchantTradeAmount","merchantTradeNum","merchantTradeTime");

    private static final String SHA256 = "SHA-256";

    public static String sign(Object object, String rsaPriKey) {
        String result = "";
        try {
            String sourceSignString = SignUtil.signString(object, signKeyList);
            String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString,null);
            byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
            result = RSACoder.encryptBASE64(newsks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] encrypt(byte[] bts) {
        MessageDigest md = null;
        byte[] result = null;
        byte[] bt = new byte[0];
        try {
            md = MessageDigest.getInstance(SHA256);
            md.update(bts);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return result;
    }

    public static String signString(Object object, List<String> signKeyList) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = SignUtil.objectToMap(object);
        TreeMap<String, Object> signMap = new TreeMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        for (String str : signKeyList) {
            Object o = map.get(str);
            if(o != null){
                signMap.put(str,o);
            }else{
                signMap.put(str,"");
            }
        }

        Iterator iterator = signMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue()) + "&");
        }

        String result = sb.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public static TreeMap<String, Object> objectToMap(Object object) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = new TreeMap<String, Object>();

        for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(object));
            }
        }
        return map;
    }
}
