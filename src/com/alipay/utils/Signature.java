package com.alipay.utils;

import com.framework.utils.ClassUtils;

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
    public static String generateSign(Object o, String privateKey) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(cls, fields, false);
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
        result = rsaSign(result.substring(0, result.length() - 1), privateKey);
        return result;
    }

    protected static String rsaSign(String content, String privateKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initSign(initPrivateKey(privateKey));
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

    public static boolean verifySign(String content, String sign, String publicKey) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initVerify(initPublicKey(publicKey));
            signature.update(content.toString().getBytes(Charset.forName("utf-8")));

            boolean bverify = signature.verify(DatatypeConverter.parseBase64Binary(sign));
            return bverify;
        }
        catch (NoSuchAlgorithmException e) {

        }
        catch (InvalidKeyException e) {

        }
        catch (SignatureException e) {

        }
        return false;
    }

    private static PublicKey initPublicKey(String key) {
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

    private static PrivateKey initPrivateKey(String key) {
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
