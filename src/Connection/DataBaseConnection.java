
package Connection;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnection {
    
    private static DataBaseConnection instance;
    private Connection connection;
   
    public static DataBaseConnection getInstance(){
        if (instance==null) {
            instance=new DataBaseConnection();
        }
    return instance;
    }

    private DataBaseConnection() {        
    }
    
    public void connectToDatabase()throws SQLException {
        String server="localhost";
        String port="3306";
        String dataBase="chat_app";
        String userName="root";
        String password="12261992";
        
        connection=java.sql.DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + dataBase, userName, password);
    }
      public Connection getConnection() {
        return connection;
    }

      public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
