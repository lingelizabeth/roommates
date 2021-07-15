package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ListItem")
public class ListItem extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_PARENT_LIST = "parentList";

    public String getText() {
        return getString(KEY_TEXT);
    }

    public void setText(String text){
        put(KEY_TEXT, text);
    }

    public String getParentList() {
        return getString(KEY_PARENT_LIST);
    }

}
