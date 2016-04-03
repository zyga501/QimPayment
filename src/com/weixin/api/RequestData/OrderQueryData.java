package com.weixin.api.RequestData;

import com.framework.utils.StringUtils;

public class OrderQueryData extends RequestData {
    public boolean checkParameter() {
        try {
            return !StringUtils.convertNullableString(transaction_id).isEmpty()
                    || !StringUtils.convertNullableString(out_trade_no).isEmpty();
        }
        catch (Exception exception) {
        }

        return false;
    }

    public String transaction_id;
    public String out_trade_no;
}
