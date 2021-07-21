package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ChoreType")
public class ChoreType extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_GROUP = "group";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String text){
        put(KEY_NAME, text);
    }

    public Group getGroup() {
        return (Group) get(KEY_GROUP);
    }

    public void setGroup(Group group){
        put(KEY_GROUP, group);
    }
}
