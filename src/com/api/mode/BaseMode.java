package com.api.mode;

import java.util.Map;

public abstract class BaseMode {
    public void initMode(Map<String,Object> requestBuffer) {
        requestBuffer_ = requestBuffer;
    }

    public String microPay() { return ""; }
    public String scanPay()  { return ""; }
    public String jsPay()  { return ""; }
    public String orderQuery()  { return ""; }
    public String orderInsert() { return ""; }

    protected Map<String,Object> requestBuffer_;
}
