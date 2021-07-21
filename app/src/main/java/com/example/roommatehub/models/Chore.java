package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Chore")
public class Chore extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_DAY = "dayOfWeek";
    public static final String KEY_USERS = "users";
    public static final String KEY_GROUP = "group";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String text){
        put(KEY_NAME, text);
    }

    public String getDayOfWeek() {
        return getString(KEY_DAY);
    }

    public void setDayOfWeek(String day){
        // Add a check for this to be either Sun, Mon, Tues, etc.
        put(KEY_DAY, day);
    }

    public List<ParseUser> getUserList(){
        return getList(KEY_USERS);
    }

    public void addUsers(ParseUser newUser){
        add(KEY_USERS, newUser);
    }

    public Group getGroup() {
        return (Group) get(KEY_GROUP);
    }

    public void setGroup(Group group){
        put(KEY_GROUP, group);
    }
}
