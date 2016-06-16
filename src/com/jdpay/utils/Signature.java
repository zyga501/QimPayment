package com.jdpay.utils;

import com.framework.utils.ClassUtils;
import com.framework.utils.MD5;
import com.framework.utils.XMLParser;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Signature {
    public static String generateSign(Object o, String key) throws Exception {
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
        result =  result.substring(0, result.length() - 1);
        result = MD5.md5LowerCase(result,key).toLowerCase();
        return result;
    }

    public static String generateSign(Map<String,Object> map, String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
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

    public static String generateRSASign(Map<String,Object> map, String key) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
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
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }
        //System.out.println("签名源串1。。。。。。。。。"+result);
        String sha256SourceSignString = SHAUtil.Encrypt(result,null);
        //System.out.println("256数据1。。。。。。。。。"+sha256SourceSignString);

        byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), key);
        return RSACoder.encryptBASE64(newsks);
//        byte[] rtbyte = new byte[0];
//        try {
//            rtbyte= result.getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String rsartn = "";
//        try {
////            for (int i = 0; i < rtbyte.length; i +=100) {
////                byte[] doFinal =RSACoder.encryptByPrivateKey(ArrayUtils.subarray(rtbyte, i,
////                        i + 100),key);
////                rsartn+=(new String(doFinal));
////            }
//            rsartn = RSACoder.encryptBASE64(RSACoder.encryptByPrivateKey(result.getBytes("utf-8"), key));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            return RSACoder.encryptBASE64(rsartn.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
    }

    public static boolean checkSignValid(String responseString, String key) throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> map = XMLParser.convertMapFromXML(responseString);
        return checkSignValid(map, key);
    }

    public static boolean checkSignValid(Map<String,Object> map, String key) {
        String signFromAPIResponse = map.getOrDefault("sign", "").toString();
        if(signFromAPIResponse=="") {
            return false;
        }
        map.remove("sign");
        String signForAPIResponse = Signature.generateSign(map, key);

        if(!signForAPIResponse.equals(signFromAPIResponse)){
            return false;
        }
        return true;
    }


    public static List<NameValuePair> mapToNameValuePairList(Object o) throws Exception {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        Class cls = o.getClass();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(cls, fields);
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                NameValuePair n=  new BasicNameValuePair(f.getName(),f.get(o).toString());
                list.add(n);
            }
        }
        return list;
    }
}