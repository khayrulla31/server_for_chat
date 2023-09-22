
package Service;

import Connection.DataBaseConnection;
import Model.Model_message;
import Model.Model_register;
import Model.Model_user_account;
import Model.Model_login;
import Model.Model_client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Servise_User {

    public Servise_User() {
        this.con=DataBaseConnection.getInstance().getConnection();       
    }
    
    public Model_message register(Model_register data){
        Model_message message=new Model_message();
        try{
            con.setAutoCommit(false);
            PreparedStatement p=con.prepareStatement(CHECK_USER);
            p.setString(1, data.getUserName());
            ResultSet r=p.executeQuery();
            if (r.next()) {
                message.setAction(false);
                message.setMessage("user already exit");
            }else{
                message.setAction(true);
            }
            r.close();
            p.close();
            if (message.isAction()) {
            //insert user register
                p=con.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                p.setString(1, data.getUserName());
                p.setString(2, data.getPassword());
                p.execute();
                r=p.getGeneratedKeys();
                r.first();
                int userID=r.getInt(1);
                r.close();
                p.close();
                
                p=con.prepareStatement(INSERT_USER_ACCOUNT);
                p.setInt(1, userID);
                p.setString(2, data.getUserName());
                p.execute();
                p.close();
                con.commit();
                con.setAutoCommit(true);
                message.setAction(true);
                message.setMessage("ok");  
                message.setData(new Model_user_account(userID, data.getUserName(), "", "", true));
            }
        }catch(SQLException e){
            message.setAction(false);
            message.setMessage("server error");
            try {
                if (con.getAutoCommit()==false) {
                    con.rollback();;
                    con.setAutoCommit(true);
                }
            } catch (SQLException el) {
                System.out.println("el "+el);
            }
        }
        return message;
    }
    public Model_user_account login(Model_login login)throws SQLException {
        Model_user_account data=null;
        PreparedStatement p=con.prepareStatement(LOGIN);
        p.setString(1, login.getUserName());
        p.setString(2, login.getPassword());
        ResultSet r=p.executeQuery();
        if (r.next()) {           
            int userId=r.getInt(1);
            String userName=r.getString(2);
            String gender=r.getString(3);
            String image=r.getString(4);
            data=new Model_user_account(userId, userName, gender, image, true);
        }
        r.close();
        p.close();
        return data;
    }
    
    
    public List<Model_user_account> getUser(int exitUser)throws SQLException {
        List<Model_user_account> list=new ArrayList<>();
        PreparedStatement p=con.prepareStatement(SELECT_USER_ACCOUNT);
        p.setInt(1, exitUser);
        ResultSet r=p.executeQuery();
        while(r.next()){
            int userId=r.getInt(1);
            String userName=r.getString(2);
            String gender=r.getString(3);
            String image=r.getString(4);
            list.add(new Model_user_account(userId, userName, gender, image, checkUserStatus(userId)));
        }
        r.close();
        p.close();
        return list;
    }
        private boolean checkUserStatus(int userId){
            List<Model_client> clients = Service.getInstance(null).getListClient();
            for (Model_client c : clients) {
                if (c.getUser().getUserId()==userId) {
                    return true;
                }
            }
        return false ;
        }
    
    //SQL
    private final String LOGIN = "select UserID, user_account.UserName, Gender, ImageString from `user` join user_account using (UserID) where `user`.UserName=BINARY(?) and `user`.`Password`=BINARY(?) and user_account.`Status`='1'";
    private final String SELECT_USER_ACCOUNT = "select UserID, UserName, Gender, ImageString from user_account where user_account.`Status`='1' and UserID<>?"; 
    private final String INSERT_USER="insert into user (UserName, `Password`) values (?,?)";
    private final String INSERT_USER_ACCOUNT = "insert into user_account (UserID, UserName) values (?,?)";
    private final String CHECK_USER="select UserID from user where UserName =? limit 1";
    //instance
    private final Connection con;
}
