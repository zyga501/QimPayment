package com.framework.socket;
import com.database.merchant.SubMerchantUser;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Server extends Thread{

    private static final int SERVER_PORT =2016;
    private static boolean isPrint =false;
    private static List<ServerThread> thread_list =new ArrayList<ServerThread>();
    private static ServerSocket serverSocket = null;

    public void run(){
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        }catch (Exception e) {
        }
        while(!this.isInterrupted()){
            try {
                Socket socket = serverSocket.accept();
                if(null != socket && !socket.isClosed()){
                    new ServerThread(socket).start();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void closeServerSocket(){
        try {
            if(null!=serverSocket && !serverSocket.isClosed())
            {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerThread extends Thread {
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private Long id;

        public ServerThread(Socket s) throws IOException {
            client = s;
            System.out.println(client.getInetAddress().getHostAddress()+"  "+client.getPort());
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }

        @Override
        public void run() {
            String jsonobj = "";
            try {
                jsonobj = in.readLine();//连接格式：{id:10920930293011}
                String userid = JSONObject.fromObject(jsonobj).get("id").toString();
                id = Long.parseLong(userid);
                SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(id);
                if (null != subMerchantUser)
                    thread_list.add(this);
            } catch (Exception e) {
                try {
                    client.close();
                    thread_list.remove(this);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }

        private void sendMessage(String msg) {
            out.println(msg);
        }
    }

    public static void pushMessage(String msg,Long uid) throws IOException {
        if (thread_list.size() > 0)
            for (ServerThread thread : thread_list) {
                if ( null!=thread.id && thread.id.equals(uid)) {
                    thread.sendMessage(msg);
                    thread.client.close();
                    break;
                }
            }
    }

}
