package com.message;

import com.database.merchant.SubMerchantUser;
import com.framework.base.ProjectSettings;
import com.framework.utils.Logger;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NotifyCenter {
    private static NotifyCenter notifyCenter_ = new NotifyCenter();

    public static void Make(){

    }

    public static void NoiftyMessage(Long uid, String nofity) {
        synchronized (notifyCenter_.ClientMap()) {
            if (notifyCenter_.ClientMap().containsKey(uid)) {
                notifyCenter_.ClientMap().get(uid).SendNotify(nofity);
            }
        }
    }

    class ClientSocket {
        public ClientSocket(Socket clientSocket) throws IOException {
            try {
                clientSocket_ = clientSocket;
                inputSteram_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
                outputStream_ = new PrintWriter(clientSocket.getOutputStream(),true);
                SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(JSONObject.fromObject(inputSteram_.readLine()).get("id").toString()));
                if (null != subMerchantUser) {
                    id_ = subMerchantUser.getId();
                }
                //outputStream_.println("success");
                //outputStream_.flush();
            }
            catch (Exception exception) {

            }
        }

        public void Close() throws IOException {
            clientSocket_.close();
        }

        public Long ID() {
            return id_;
        }

        public void SendNotify(String nofityMessage) {
            outputStream_.println(nofityMessage);
        }

        private Long id_ = Long.MAX_VALUE;
        private Socket clientSocket_;
        private BufferedReader inputSteram_;
        private PrintWriter outputStream_;
    }

    class NofityCenterThread extends Thread {
        @Override
        public void run() {
            try {
                notifySocket_ = new ServerSocket(ProjectSettings.getNotifyPort());
                System.out.println("start Socket");
            }
            catch (Exception exception) {
                Logger.error("Start Notify Center Failed!");
                return;
            }

            while(!isInterrupted()){
                try {
                    Socket socket = notifySocket_.accept();
                    if(null != socket && !socket.isClosed()) {
                        synchronized (clientMap_) {
                            try {
                                ClientSocket clientSocket = new ClientSocket(socket);
                                if (clientMap_.containsKey(clientSocket.ID())) {
                                    clientMap_.get(clientSocket.ID()).Close();
                                    clientMap_.remove(clientSocket.ID());
                                }
                                clientMap_.put(clientSocket.ID(), clientSocket);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            synchronized (clientMap_) {
                try {
                    for (Map.Entry<Long, ClientSocket> entry : clientMap_.entrySet()) {
                        entry.getValue().Close();
                    }
                } catch (Exception exception) {

                }
            }
        }
    }

    public NotifyCenter() {
        new Thread(new NofityCenterThread()).start();
    }

    public Map<Long, ClientSocket> ClientMap() { return clientMap_; }

    private ServerSocket notifySocket_;
    private Map<Long, ClientSocket> clientMap_ = new HashMap<>();
}
