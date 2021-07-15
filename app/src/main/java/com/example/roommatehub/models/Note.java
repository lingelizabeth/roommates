package com.example.roommatehub.models;

import com.example.roommatehub.models.Group;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("List")
public class Note extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_GROUP = "group";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public Group getGroup() {
        return (Group) getParseObject(KEY_GROUP);
    }

    public void setGroup(Group group){
        put(KEY_GROUP, group);
    }

    public List<Object> getItemList() {
        return getList("itemsList");
    }


}
