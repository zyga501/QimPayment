package com.weixin.api.RequestData;

import com.framework.utils.ClassUtils;
import com.framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestData {
    public RequestData() {
        nonce_str = StringUtils.generateRandomString(32);
    }

    public Map<String,Object> convertToMap() {
        Map<String,Object> map = new HashMap<String, Object>();
        ArrayList<Field> fields = new ArrayList<Field>();
        ClassUtils.getBeanFields(this.getClass(), fields);
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

    public boolean checkParameter() {
        try {
            return !appid.isEmpty()
                    && !mch_id.isEmpty()
                    && !sub_mch_id.isEmpty()
                    && !nonce_str.isEmpty();
        }
        catch (Exception exception) {

        }

        return false;
    }

    public String appid; // 公众账号ID 微信分配的公众账号ID
    public String mch_id; // 商户号 微信支付分配的商户号
    public String sub_mch_id; // 子商户号 微信支付分配的子商户号，开发者模式下必填
    public String nonce_str; // 随机字符串，不长于32位。
    public String sign; // 签名

    //option
    public String sub_appid; // 子商户公众账号ID 微信分配的子商户公众账号ID
}
