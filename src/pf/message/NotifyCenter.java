package pf.message;

import net.sf.json.JSONObject;
import pf.ProjectLogger;
import pf.ProjectSettings;
import pf.database.merchant.SubMerchantUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    public static void NotifyMessage(Long uid, String notify) {
        synchronized (notifyCenter_.ClientMap()) {
            if (notifyCenter_.ClientMap().containsKey(uid)) {
                notifyCenter_.ClientMap().get(uid).SendNotify(notify);
            }
            else {
                ProjectLogger.info("NotifyMessage Not find " + uid);
            }
        }
    }

    class ClientSocket extends Thread {
        public ClientSocket(Socket socket){
            this.clientSocket_ = socket;
        }

        @Override
        public void run() {
            try {
                outputStream_ = new PrintWriter(clientSocket_.getOutputStream(),true);
                inputStream_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
                while(!clientSocket_.isClosed()) {
                    String buffer = inputStream_.readLine();
                    if (null == buffer) {
                        break;
                    }

                    if (buffer.length() <= 0) {
                        Thread.sleep(5000);
                        continue;
                    }

                    if (id_ == Long.MAX_VALUE) {
                        SubMerchantUser subMerchantUser = SubMerchantUser.getSubMerchantUserById(Long.parseLong(JSONObject.fromObject(buffer).get("id").toString()));
                        if ((null != subMerchantUser)) {
                            id_ = subMerchantUser.getId();
                            if (clientMap_.containsKey(id_)) {
                                clientMap_.get(id_).close();
                            }
                            clientMap_.put(id_, this);
                            ProjectLogger.info("remoteID:" + String.valueOf(id_));
                        }
                    }

                    if (null != buffer && buffer.contains("keepalive")) {
                        SendNotify("OK");
                    }
                }
            }
            catch (Exception e){

            }
            finally {
                if (clientMap_.containsKey(id_)) {
                    clientMap_.get(id_).close();
                    clientMap_.remove(id_);
                }
            }
        }

        public void close() {
            try {
                if (inputStream_ != null) {
                    inputStream_.close();
                }
                if (outputStream_ != null) {
                    outputStream_.close();
                }
                if (clientSocket_ != null) {
                    clientSocket_.close();
                }
            }
            catch (Exception exception) {

            }
        }

        public void SendNotify(String notifyMessage) {
            outputStream_.println(notifyMessage);
        }

        private Long id_ = Long.MAX_VALUE;
        private Socket clientSocket_;
        private BufferedReader inputStream_;
        private PrintWriter outputStream_;
    }

    class NotifyCenterThread extends Thread {
        @Override
        public void run() {
            try {
                notifySocket_ = new ServerSocket(ProjectSettings.getNotifyPort());
            }
            catch (Exception exception) {
                exception.printStackTrace();
                ProjectLogger.error("Start Notify Center Failed!");
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
                        entry.getValue().close();
                    }
                } catch (Exception exception) {

                }
            }
        }
    }

    private NotifyCenter() {
        new Thread(new NotifyCenterThread()).start();
    }

    public Map<Long, ClientSocket> ClientMap() { return clientMap_; }

    private ServerSocket notifySocket_;
    private Map<Long, ClientSocket> clientMap_ = new HashMap<>();
}