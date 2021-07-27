package com.example.roommatehub.models;

import com.example.roommatehub.R;
import com.example.roommatehub.fragments.ChorePageFragment;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Notification")
public class Notification extends ParseObject {
    public static final String KEY_CONTENT = "notificationContent";
    public static final String KEY_GROUP = "group";
    public static final String LANGUAGE = "en";

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

    // setGroup conflicts with an getGroup
    // getParseGroup gets the current group from Parse
    public Group getParseGroup() {
        return (Group) getParseObject(KEY_GROUP);
    }

    public void setParseGroup(Group group){
        put(KEY_GROUP, group);
    }

    // Group is already an attribute of this class, this method saves it to the database
    public void saveParseGroup(){
        put(KEY_GROUP, group);
    }

    public static String[] getCreateChoreNotificationData(ParseUser currentUser, Chore chore, Group group, String icon){
        String title = currentUser.getUsername()+" created \""+chore.getName()+"\" in"+group.getTitle();
        String message = "";
        if(chore.getUserList().contains(currentUser)){
            message = "You are assigned this on "+chore.getDayOfWeek();
        } else{
            message = "You are not assigned anything";
        }
        String[] data = {
                title, message, icon
        };
        return data;
    }

    public static String[] getCompleteChoreNotificationData(ParseUser currentUser, Chore chore, Group group, String icon){
        String title = currentUser.getUsername()+" marked \""+chore.getName()+"\" as completed in "+group.getTitle();
        String message = chore.getDayOfWeek()+"\\'s chore progress is "+ ChorePageFragment.getProgress()+"%";
        String[] data = {
                title, message, icon
        };
        return data;
    }


        private String title;
        private String[] data;
        private String smallIconRes;
        private String iconUrl;
        private String buttons;
        private boolean shouldShow;
        private Group group;

        public Notification(String title, String[] data, String smallIconRes, String iconUrl, String buttons, boolean shouldShow, Group group) {
            this.title = title;
            this.data = data;
            this.smallIconRes = smallIconRes;
            this.iconUrl = iconUrl;
            this.buttons = buttons;
            this.shouldShow = shouldShow;
            this.group = group;
        }

        // Required empty constructor - should never be called
        public Notification(){
            this.title = "";
            this.data = null;
            this.smallIconRes = "";
            this.iconUrl = "";
            this.buttons = "";
            this.shouldShow = true;
            this.group = null;
        }

        public void parseContent(){
            JSONObject notificationContent = getContent();
            try {
                this.title = (String)notificationContent.get("android_group");
                String[] data = new String[3];
                data[0] = (String)((JSONObject)notificationContent.get("headings")).get(LANGUAGE);
                data[1] = (String)((JSONObject)notificationContent.get("contents")).get(LANGUAGE);
                data[2] = (String)notificationContent.get("large_icon");
                this.data = data;
                this.smallIconRes = (String)notificationContent.get("small_icon");
                this.iconUrl = (String)notificationContent.get("large_icon");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getGroup() {
            return title;
        }

        public String getTitle() {
            return data[0];
        }

        public String getMessage() {
            return data[1];
        }

        public String getSmallIconRes() {
            return smallIconRes;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public String getLargeIconUrl() {
            return data[2];
        }

        public String getButtons() {
            return buttons;
        }

        public boolean shouldShow() {
            return shouldShow;
        }

}

