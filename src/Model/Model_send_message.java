
package Model;

public class Model_send_message {

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Model_send_message() {
    }

    public Model_send_message(int fromUserId, int toUserId, String text) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.text = text;
    }
    
    
    private int fromUserId;
    private int toUserId;
    private String text;
    

}
