
package Model;


public class Model_message {

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public Model_message(boolean action, String message, Object data) {
        this.action = action;
        this.message = message;
        this.data = data;
    }
 
    public Model_message() {
    }
    
    private boolean action;
    private String message;
    private Object data;
}
