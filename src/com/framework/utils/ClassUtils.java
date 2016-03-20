package com.framework.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
}
