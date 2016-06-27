package com.framework.socket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

public class SocketServiceLoader implements ServletContextListener{
    private static Server server;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if(null!=server && !server.isInterrupted())
        {
            server.closeServerSocket();
            server.interrupt();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        if(null==server)
        {
            try {
                server=new Server();
            } catch (Exception e) {
                e.printStackTrace();
            }
            server.start();
        }
    }

    public static void pushMessage(String msg,Long uid) throws IOException {
        if(null!=server)
            server.pushMessage(msg,uid);
    }
}