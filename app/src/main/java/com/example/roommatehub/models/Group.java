package com.example.roommatehub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_MEMBERS = "groupMembers";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public ParseRelation<ParseUser> getMembers() {return getRelation(KEY_MEMBERS);};

//    public void setMembers(List<String> members ){ put(KEY_MEMBERS, members); }
//
    public void addMembers(List<ParseUser> members){
        // addAll(KEY_MEMBERS, members);
        for(ParseUser member: members){
            getRelation(KEY_MEMBERS).add(member);
        }
    }

}
