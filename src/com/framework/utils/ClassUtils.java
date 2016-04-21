package com.framework.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {
    public static void getBeanFields(Class cls, ArrayList<Field> fields) {
        for (int index = 0; index < cls.getDeclaredFields().length; ++index) {
            fields.add(cls.getDeclaredFields()[index]);
        }
        if(cls.getSuperclass()!=null){
            Class clsSup = cls.getSuperclass();
            getBeanFields(clsSup, fields);
        }
    }


    public static Map<String,Object> convertToMap(Object object) {
        Map<String,Object> map = new HashMap<String, Object>();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(object.getClass(), fields);
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
}
