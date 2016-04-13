package com.weixin.api.RequestData;

import net.sf.json.JSONObject;

import java.util.LinkedHashMap;

public class TemplateMessageRequestData {
    public TemplateMessageRequestData() {
        url = "";
        topcolor = "#FF0F0F";
    }

    public String buildRequestData() {
        if (touser.isEmpty() || template_id.isEmpty()) {
            return "";
        }

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("touser", touser);
        resultMap.put("template_id", template_id);
        resultMap.put("url", url);
        resultMap.put("topcolor", topcolor);
        resultMap.put("data", body);

        return JSONObject.fromObject(resultMap).toString();
    }

    public String touser;
    public String template_id;
    public String url;
    public String topcolor;
    public LinkedHashMap<String, Object> body;
}
