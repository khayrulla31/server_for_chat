
package Model;

import com.corundumstudio.socketio.SocketIOClient;



public class Model_client {

    public SocketIOClient getClient() {
        return client;
    }

    public void setClient(SocketIOClient client) {
        this.client = client;
    }

    public Model_user_account getUser() {
        return user;
    }

    public void setUser(Model_user_account user) {
        this.user = user;
    }

    public Model_client(SocketIOClient client, Model_user_account user) {
        this.client = client;
        this.user = user;
    }

    public Model_client() {
    }
    
    private SocketIOClient client;
    private Model_user_account user;
    
}
