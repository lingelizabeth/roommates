package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ListItem")
public class ListItem extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_EDITING = "currentlyEditing";

    public String getText() {
        return getString(KEY_TEXT);
    }

    public void setText(String text){
        put(KEY_TEXT, text);
    }

    public ParseUser getCurrentlyEditing(){
        return getParseUser(KEY_EDITING);
    }

    public String getCurrentlyEditingUsername(){
        // Get username of user currently editing
        // Returns a blank String if no one is editing
        ParseUser currentlyEditing = null;
        try {
            currentlyEditing = fetchIfNeeded().getParseUser(KEY_EDITING);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(currentlyEditing == null){
            return "";
        }

        // Use a try catch to fetch the username with fetchIfNeeded
        String name = "";
        try {
            name = currentlyEditing.fetchIfNeeded().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void setCurrentlyEditing(ParseUser user){
        put(KEY_EDITING, user);
    }

}
