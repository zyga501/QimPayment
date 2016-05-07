package com.alipay.utils;

import com.framework.utils.ClassUtils;
import com.framework.utils.MD5;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

public class Signature {
    public static String generateSign(Object o, String key) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(cls, fields);
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }

    private String rsaSign(String content, String key) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initSign(initPrivateKey(key));
            signature.update(content.getBytes(Charset.forName("utf-8")));
            byte[] signed = signature.sign();
            return DatatypeConverter.printBase64Binary(signed);
        }
        catch (NoSuchAlgorithmException e) {

        }
        catch (InvalidKeyException e) {

        }
        catch (SignatureException e) {

        }
        return null;
    }

    protected boolean rsaVerify(String content, String sign, String key) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initVerify(initPublicKey(key));
            signature.update(content.getBytes(Charset.forName("utf-8")));

            boolean bverify = signature.verify(DatatypeConverter.parseBase64Binary(sign));
            return bverify;
        } catch (NoSuchAlgorithmException e) {

        }
        catch (InvalidKeyException e) {

        }
        catch (SignatureException e) {

        }
        return false;
    }

    private PublicKey initPublicKey(String key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = DatatypeConverter.parseBase64Binary(key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            return pubKey;
        }
        catch (NoSuchAlgorithmException e) {

        }
        catch (InvalidKeySpecException e) {

        }
        return null;
    }

    private PrivateKey initPrivateKey(String key) {
        try {
            byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        }
        catch (NoSuchAlgorithmException e) {

        }
        catch (InvalidKeySpecException e) {

        }
        return null;
    }
}
