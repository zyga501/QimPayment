package com.framework.utils;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {
    public static void getBeanFields(Class cls, ArrayList<Field> fields) {
        getBeanFields(cls, fields, true);
    }

    public static void getBeanFields(Class cls, ArrayList<Field> fields, boolean recursive) {
        for (int index = 0; index < cls.getDeclaredFields().length; ++index) {
            fields.add(cls.getDeclaredFields()[index]);
        }

        if(recursive && cls.getSuperclass()!=null) {
            Class clsSup = cls.getSuperclass();
            getBeanFields(clsSup, fields);
        }
    }

    public static Map<String,Object> convertToMap(Object object) {
        return convertToMap(object, true);
    }

    public static Map<String,Object> convertToMap(Object object, boolean recursive) {
        Map<String,Object> map = new HashMap<String, Object>();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(object.getClass(), fields, recursive);
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(object);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String convertToQuery(Object object, String charset, boolean recursive) throws Exception {
        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(object.getClass(), fields, recursive);
        for (Field field : fields) {
            Object obj = field.get(object);
            if (obj != null) {
                String name = field.getName();
                String value = obj.toString();
                if (!value.isEmpty()) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }

                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }
        }

        return query.toString();
    }
}
