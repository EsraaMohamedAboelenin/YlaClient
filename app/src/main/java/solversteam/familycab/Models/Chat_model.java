package solversteam.familycab.Models;

/**
 * Created by Nashaat on 10/30/2017.
 */

public class Chat_model
{
    private String message,msg_id;

    public Chat_model(String message, String msg_id) {
        this.message = message;
        this.msg_id = msg_id;
    }

    public String getMessage() {
        return message;
    }

    public String getMsg_id() {
        return msg_id;
    }
}
