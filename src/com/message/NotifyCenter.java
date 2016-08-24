package com.message;

import com.database.merchant.SubMerchantUser;
import com.framework.base.ProjectSettings;
import com.framework.utils.Logger;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NotifyCenter {
    private static NotifyCenter notifyCenter_;

    public synchronized static void Make(){
        if (notifyCenter_ == null) {
            notifyCenter_ = new NotifyCenter();
        }
    }

    public static void NoiftyMessage(Long uid, String nofity) {
        synchronized (notifyCenter_.ClientMap()) {
            if (notifyCenter_.ClientMap().containsKey(uid)) {
                notifyCenter_.ClientMap().get(uid).SendNotify(nofity);
            }
        }
    }

    class ClientSocket extends Thread {
        public Socket GetSocket( ){
            return this.clientSocket_ ;
        }
        public ClientSocket(Socket socket){
            this.clientSocket_ = socket;
        }

        @Override
        public void run() {
            try {
                String buffer;
                outputStream_ = new PrintWriter(clientSocket_.getOutputStream(),true);
                inputStream_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
                while(true){
                    try {
                        if (this.clientSocket_.isClosed()) {
                            clientMap_.remove(this.ID());
                            return;
                        }
                        buffer = inputStream_.readLine();
                        if (null==buffer){
                            continue;
                        }
                        SubMerchantUser subMerchantUser = null;
                        try {
                            subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(JSONObject.fromObject(buffer).get("id").toString()));
                        }
                        catch (Exception e){

                        }
                        if ((null != subMerchantUser)) {
                            if (clientMap_.containsKey(subMerchantUser.getId())) {
                                clientMap_.get(subMerchantUser.getId()).Close();
                                clientMap_.remove(subMerchantUser.getId());
                            }
                            clientMap_.put(subMerchantUser.getId(), this);
                            id_ = subMerchantUser.getId();
                            SendNotify("OK");
                            continue;
                        }
                        if (this.clientSocket_.isClosed()){
                            clientMap_.remove(this.ID());
                        }
                        if (null!=buffer && buffer.contains("keepalive")) {
                            System.out.println("client:"+buffer.toString());
                            System.out.println("client:"+this.ID());
                            SendNotify("OK");
                        }
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                }
            }
            catch (Exception exception) {

            }
        }
      /*  public ClientSocket(Socket clientSocket) throws IOException {
            try {
                clientSocket_ = clientSocket;
                inputSteram_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
                if (inputSteram_.readLine().contains("keepalive")==true) {
                    clientSocket_.getOutputStream().write("ok".getBytes());
                    return ;
                }
                outputStream_ = new PrintWriter(clientSocket.getOutputStream(),true);
                SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(JSONObject.fromObject(inputSteram_.readLine()).get("id").toString()));
                if (null != subMerchantUser) {
                    id_ = subMerchantUser.getId();
                }
            }
            catch (Exception exception) {

            }
        }*/

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
        private BufferedReader inputStream_;
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
                exception.printStackTrace();
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
                                clientSocket.start();
//                                    if (clientMap_.containsKey(clientSocket.ID())) {
//                                        clientMap_.get(clientSocket.ID()).Close();
//                                        clientMap_.remove(clientSocket.ID());
//                                }
//                                clientMap_.put(clientSocket.ID(), clientSocket);
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

    private NotifyCenter() {
        new Thread(new NofityCenterThread()).start();
    }

    public Map<Long, ClientSocket> ClientMap() { return clientMap_; }

    private ServerSocket notifySocket_;
    private Map<Long, ClientSocket> clientMap_ = new HashMap<>();
}
