package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("NotificationData")
public class NotificationData extends ParseObject {
    public static final String KEY_CONTENT = "notificationContent";

    public JSONObject getContent(){
        // Throws JSON exception if String can't be parsed into JSONObject
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(getString(KEY_CONTENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setContent(JSONObject notificationContent){
        put(KEY_CONTENT, notificationContent.toString());
    }
}
