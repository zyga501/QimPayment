package com.framework.base;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream; 
import java.util.Iterator;
import java.util.Properties; 

public class Readconfig {
	  public static void main(String[] args) {
	        System.out.println(Readconfig.class.getResource("").getPath());
	        System.out.println(Readconfig.class.getResource("/").getPath().substring(1));
	    }
    public static String fetchspecialinfo(String keyname) {
        Properties prop = new Properties();
        try{
            //读取属性文件a.properties 
            InputStream in = new BufferedInputStream (new FileInputStream(Readconfig.class.getResource("/").getPath().
                    substring(1).replaceAll("%20", 	" ")+"special.properties"));
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                if (key.toLowerCase().equals(keyname))
                    return prop.getProperty(key);
            }
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static String yzfconfig(String submerchantid) {
        Properties prop = new Properties();
        try{
            //读取属性文件a.properties
            InputStream in = new BufferedInputStream (new FileInputStream(Readconfig.class.getResource("/").getPath().
                    substring(1).replaceAll("%20", 	" ")+"yzfconfig.properties"));
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                if (key.toLowerCase().equals(submerchantid))
                    return prop.getProperty(key);
            }
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
