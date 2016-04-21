package com.weixin.servlet;

import com.framework.ProjectSettings;
import com.weixin.api.AccessToken;
import com.weixin.database.MerchantInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

public class AccessTokenServlet extends HttpServlet {
    public class AccessTokenThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    List<String> appidList = new ArrayList<>();
                    appidList.add(MerchantInfo.getMerchantInfoById(ProjectSettings.getId()).getAppid());
                    AccessToken.updateAccessToken(appidList);
                    // 休眠7000秒
                    Thread.sleep((DEFAULTEXPIRESTIME - 200) * 1000);
                }
                catch (Exception exception) {
                    try {
                        // 60秒后再获取
                        Thread.sleep(60 * 1000);
                    }
                    catch (InterruptedException e1) {
                    }
                }
            }
        }

        private final static int DEFAULTEXPIRESTIME = 7200;
    }

    public void init() throws ServletException {
        new Thread(new AccessTokenThread()).start();
    }
}
