package pf.bestpay.utils;

import pf.framework.utils.JsonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LedgerUtils {
    public static String buildLedgerInfo(String subMerchantId, double orderAmt, double rate, String merchantId, double merchantRate, String subMerchantRates) {
        StringBuilder legerInfo = new StringBuilder();
        if (subMerchantId == null || subMerchantId.length() <= 0) {
            return legerInfo.toString();
        }

        double leftAmt = orderAmt;
        Map<String, Integer> ledgerInfoList = new LinkedHashMap<>();
        if (merchantRate >= 0.0) {
            int ledgerAmt = (int)(orderAmt * merchantRate / (1 - rate));
            if (ledgerAmt >= 1) {
                leftAmt -= ledgerAmt;
                ledgerInfoList.put(merchantId, ledgerAmt);
            }
        }
        try {
            Map<String, Object> rateMap = JsonUtils.toMap(subMerchantRates, true);
            for (Map.Entry<String, Object> entry : rateMap.entrySet()) {
                int ledgerAmt = (int)(orderAmt * Double.parseDouble(entry.getValue().toString()) * (1 - rate));
                if ((int)ledgerAmt >= 1) {
                    leftAmt -= ledgerAmt;
                    ledgerInfoList.put(merchantId, ledgerAmt);
                }
            }
        }
        catch (Exception exception) {

        }

        if ((int)leftAmt >= 1) {
            ledgerInfoList.put(subMerchantId, (int)leftAmt);
        }

        if (ledgerInfoList.size() > 1) {
            for (Map.Entry<String, Integer> entry : ledgerInfoList.entrySet()) {
                if (legerInfo.length() > 0) {
                    legerInfo.append("|");
                }
                legerInfo.append(entry.getKey() + ":" + entry.getValue().toString());
            }
        }
        
        return legerInfo.toString();
    }
}
