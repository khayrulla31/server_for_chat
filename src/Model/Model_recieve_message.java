
package Model;

public class Model_recieve_message{

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Model_recieve_message() {

    }

    public Model_recieve_message(int fromUserId,String text) {
        this.fromUserId = fromUserId;
        this.text = text;
    }
    
    
    private int fromUserId;
    private String text;
    
}
