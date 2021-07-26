package com.example.roommatehub.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Chore")
public class Chore extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_DAY = "dayOfWeek";
    public static final String KEY_USERS = "users";
    public static final String KEY_GROUP = "group";
    public static final String KEY_TYPE = "choreType";
    public static final String KEY_CHECKED = "checked";

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

    public void addUsersFromUserIcons(List<UserIcon> assignedUserIcons){
        List<ParseUser> assignedUsers = new ArrayList<>();
        for(UserIcon userIcon: assignedUserIcons){
            assignedUsers.add(userIcon.getUser());
        }
        addAll(KEY_USERS, assignedUsers);
    }

    public Group getGroup() {
        return (Group) get(KEY_GROUP);
    }

    public void setGroup(Group group){
        put(KEY_GROUP, group);
    }

    public boolean isChecked() {
        return getBoolean(KEY_CHECKED);
    }

    public void setChecked(boolean checked){
        put(KEY_CHECKED, checked);
    }

    public ChoreType getChoreType() {
        return (ChoreType) get(KEY_TYPE);
    }

    public void setChoreType(ChoreType choreType){
        put(KEY_TYPE, choreType);
    }

    public void setChoreType(String choreTypeName, Group group){
        // Find chore type with this name and group
        ParseQuery query = ParseQuery.getQuery(ChoreType.class);
        query.whereEqualTo(ChoreType.KEY_NAME, choreTypeName);
        query.whereEqualTo(ChoreType.KEY_GROUP, group);
        try {
            ChoreType result = (ChoreType) query.getFirst();
            put(KEY_TYPE, result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
