package com.weixin.api.RequestData;

import com.framework.utils.I18n;

public class TemplateMessageRequestData {
    public TemplateMessageRequestData() {
        url = "";
        topcolor = "#FF0F0F";
    }

    public String buildRequestData() {
        if (touser.isEmpty() || template_id.isEmpty()) {
            return "";
        }

        String reqeustData = String.format(I18n.GetText("TemplateMessage"),
                touser,
                template_id,
                topcolor,
                nickName, "#173177",
                timeEnd, "#173177",
                totalFee.toString(), "#FF0FFF",
                storeName, "#173177",
                transactionId, "#173177");

        return reqeustData;
    }

    public String touser;
    public String template_id;
    public String url;
    public String topcolor;
    public String nickName;
    public String timeEnd;
    public Double totalFee;
    public String storeName;
    public String transactionId;
}
