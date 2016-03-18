package com.weixin.api;

import com.framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommonData {
    public CommonData() {
        nonce_str = StringUtils.generateRandomString(32);
    }

    public Map<String,Object> convertToMap() {
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
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

    String appid; // 公众账号ID 微信分配的公众账号ID
    String sub_appid; // 子商户公众账号ID 微信分配的子商户公众账号ID
    String mch_id; // 商户号 微信支付分配的商户号
    String sub_mch_id; // 子商户号 微信支付分配的子商户号，开发者模式下必填
    String nonce_str; // 随机字符串，不长于32位。
    String sign; // 签名
}
