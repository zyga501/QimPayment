package com.weixin.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AccessTokenServlet extends HttpServlet {
    public class AccessTokenThread implements Runnable {
        public void run() {
            while (true) {
                try {

                }
                catch (Exception exception) {
                    try {
                        Thread.sleep(60 * 1000);
                    }
                    catch (InterruptedException e1) {

                    }
                }
            }
        }
    }

    public void init() throws ServletException {
        new Thread(new AccessTokenThread()).start();
    }
}
