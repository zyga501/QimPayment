package com.weixin.utils;

import com.framework.utils.ClassUtils;
import com.framework.utils.MD5;
import com.framework.utils.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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

    public static boolean checkResponseSignValid(String responseString, String key) throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> map = XMLParser.convertMapFromXML(responseString);

        String signFromAPIResponse = map.get("sign").toString();
        if(signFromAPIResponse=="" || signFromAPIResponse == null) {
            return false;
        }
        map.remove("sign");
        String signForAPIResponse = Signature.generateSign(map, key);

        if(!signForAPIResponse.equals(signFromAPIResponse)){
            return false;
        }
        return true;
    }

}
