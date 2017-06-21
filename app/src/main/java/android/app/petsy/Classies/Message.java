package android.app.petsy.Classies;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Justinas on 2017-06-14.
 */

public class Message {

    private String ID1;
    private String ID2;
    private String message;

    public Message(JSONObject object){
        try {
            ID1 = object.getString("ID1");
            ID2 = object.getString("ID2");
            message = object.getString("Message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getID1() {
        return ID1;
    }

    public String getID2() {
        return ID2;
    }

    public String getMessage() {
        return message;
    }
}
