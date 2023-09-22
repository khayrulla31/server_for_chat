
package Service;

import Model.Model_client;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import Model.Model_register;
import Model.Model_message;
import Model.Model_user_account;
import Model.Model_login;
import com.corundumstudio.socketio.listener.DisconnectListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import Model.Model_send_message;
import Model.Model_recieve_message;




public class Service {
    
    private static Service instance;
    private SocketIOServer server;
    private Servise_User serviseUser;
    private JTextArea textArea;
    private final int PORT_NUMBER=9999;
    private List<Model_client> listClient;

    
    public static Service getInstance(JTextArea textArea){
        if (instance==null) {
        instance=new Service(textArea);
        }
    return instance;
    }
    
    private Service(JTextArea textArea){
        this.textArea=textArea;
        serviseUser=new Servise_User();
        listClient=new ArrayList<>();
    }
    
    public void startServer(){
        Configuration config=new Configuration();
        config.setPort(PORT_NUMBER);
        server=new SocketIOServer(config);
        server.addConnectListener(new ConnectListener(){
            @Override
            public void onConnect(SocketIOClient sioc) {
                textArea.append("one client connection \n");
           }
        });
        server.addEventListener("register", Model_register.class, new DataListener<Model_register>() {
            @Override
            public void onData(SocketIOClient sioc, Model_register t, AckRequest ar) throws Exception {
                Model_message message=serviseUser.register(t);
                ar.sendAckData(message.isAction(), message.getMessage(),message.getData());
                if (message.isAction()) {
                    textArea.append("user has register: "+t.getUserName()+"  pass  "+t.getPassword()+" \n");
                    server.getBroadcastOperations().sendEvent("list_user", (Model_user_account)message.getData());
                    addClient(sioc, (Model_user_account)message.getData());
                }               
            }
        });
        
        server.addEventListener("login", Model_login.class, new DataListener<Model_login>(){
            @Override
            public void onData(SocketIOClient sioc, Model_login t, AckRequest ar) throws Exception {
                Model_user_account login=serviseUser.login(t);
                if (login!=null) {
                    ar.sendAckData(true, login);
                    addClient(sioc, login);
                    userConnect(login.getUserId());
                }else{
                    ar.sendAckData(false);
                }
            }
        });
        server.addEventListener("list_user", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer userID, AckRequest ar) throws Exception {
                try {
                    List<Model_user_account>list=serviseUser.getUser(userID);    
                    sioc.sendEvent("list_user", list.toArray());
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        
        server.addEventListener("send_to_user", Model_send_message.class, new DataListener<Model_send_message>() {
            @Override
            public void onData(SocketIOClient sioc, Model_send_message t, AckRequest ar) throws Exception {
                sendToClient(t);
            }
        });
        
        
        
        
        server.addDisconnectListener(new DisconnectListener(){
            @Override
            public void onDisconnect(SocketIOClient sioc) {
                int userId=removeClient(sioc);
                if (userId!=0) {
                    userDisconnect(userId);
                }
            }
        });
        server.start();
        textArea.append("Server has start on port : "+PORT_NUMBER+"\n");       
    }
    
    private void userConnect(int userId){
        server.getBroadcastOperations().sendEvent("user_status", userId, true);
    }
    
    private void userDisconnect(int userId){
        server.getBroadcastOperations().sendEvent("user_status", userId, false);
    }
    
    private void addClient(SocketIOClient client, Model_user_account user){
        listClient.add(new Model_client(client, user));
    }
    
    private void sendToClient(Model_send_message data){
        for (Model_client c : listClient) {
            if(c.getUser().getUserId()==data.getToUserId()){
                c.getClient().sendEvent("recieve_ms", new Model_recieve_message(data.getFromUserId(),data.getText()));
            }
        }
    }
    
    
    public int removeClient(SocketIOClient client){
        for (Model_client d : listClient) {
            listClient.remove(d);
            return d.getUser().getUserId();
        }
        return 0;
    }
    
    public List<Model_client> getListClient() {
        return listClient;
    }
}
