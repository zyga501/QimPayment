package com.jdpay.utils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SignUtil {


    // ��Ҫ����ǩ����ֶ�
    private  static List<String> signKeyList = Arrays.asList("merchantNotifyUrl", "merchantNum","merchantOuterOrderNum", "merchantTradeAmount","merchantTradeNum","merchantTradeTime");

    //����ժҪ�㷨ΪSHA-256
    private static final String SHA256 = "SHA-256";

    /**
     * ˽Կ�Լ�����ݽ���ǩ��(����)
     *
     * @param object        ��Ҫǩ��(����)�Ķ���
     * @param rsaPriKey     ˽Կ
     * @param rsaPriKey     ˽Կ
     * @return
     */
    public static String sign(Object object, String rsaPriKey) {
        String result = "";
        try {
            //��ȡǩ����Ҫ�ַ������
            String sourceSignString = SignUtil.signString(object, signKeyList);
            System.out.println("签名源串。。。。。。。。。"+sourceSignString);
            //ժҪ
            String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString,null);
            System.out.println("256数据。。。。。。"+sha256SourceSignString);
            //˽Կ��ժҪ���м���
            byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
            System.out.println("newsks数据。。。。。。"+newsks);
            result = RSACoder.encryptBASE64(newsks);
            System.out.println("result数据。。。。。。"+result);
            
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


    /**
     * ���ǩ��ԭ��
     *
     * @param object
     * @param signKeyList
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    /*public static String signString(Object object, List<String> signKeyList) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = SignUtil.objectToMap(object);
        // ƴԭString
        StringBuilder sb = new StringBuilder();
        // ֵ������Ҫ����ǩ����ֶ�
        for (String strKey : signKeyList) {
            String value =(String) map.get(strKey);
            if(value != null){
                sb.append(strKey + "=" + value + "&");
            }else{
                sb.append(strKey + "=" + "" + "&");
            }
        }
        // ȥ�����һ��&
        String result = sb.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }*/

    /**
     * ���ǩ��ԭ��
     *
     * @param object
     * @param signKeyList
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String signString(Object object, List<String> signKeyList) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = SignUtil.objectToMap(object);
        TreeMap<String, Object> signMap = new TreeMap<String, Object>();
        // ƴԭString
        StringBuilder sb = new StringBuilder();
        // ɾ����Ҫ����ǩ�������
        for (String str : signKeyList) {
            Object o = map.get(str);
            if(o != null){
                signMap.put(str,o);
            }else{
                //Լ����ָ���ֶ���ֵʱ���ÿ��ַ�
                signMap.put(str,"");
/*                logger.error("|PcWePayValidSignFacade|cfmSign4SelectedKeys|signString4SelectedKeys|ָ����ǩ���ֶβ�����|"+str);
                throw new IllegalArgumentException("ָ����ǩ���ֶβ����ڡ�");*/
            }

        }
        // ����
        Iterator iterator = signMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue()) + "&");
        }
        // ȥ�����һ��&
        String result = sb.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }

        System.out.println("============result="+result);
        return result;
    }

    /**
     * ����תmap
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static TreeMap<String, Object> objectToMap(Object object) throws IllegalArgumentException, IllegalAccessException {
        TreeMap<String, Object> map = new TreeMap<String, Object>();

        // ��������
        for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            // �������key��list
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(object));
            }
        }
        return map;
    }
}
