package pf.jdpay.utils;

import pf.framework.utils.ClassUtils;
import pf.framework.utils.MD5;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

        String sha256SourceSignString = SHAUtil.Encrypt(result,null);
        return RSACoder.encryptBASE64(RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), key));
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