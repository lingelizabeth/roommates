package com.example.roommatehub.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_MEMBERS = "groupMembers";
    public static final String KEY_ACTIVITY = "memberActivity";

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

    // Runs a Parse Query to get the member list
    public List<ParseUser> getMemberList() throws ParseException {
        ParseRelation relation = getRelation("groupMembers");
        ParseQuery query = relation.getQuery();
        List<ParseUser> members = query.find();
        return members;
    };

    public ParseRelation<ParseUser> getMembers() {return getRelation(KEY_MEMBERS);};

//    public void setMembers(List<String> members ){ put(KEY_MEMBERS, members); }
//
    public void addMembers(List<ParseUser> members){
        // addAll(KEY_MEMBERS, members);
        for(ParseUser member: members){
            getRelation(KEY_MEMBERS).add(member);
        }
    }

    public JSONObject getActivityJSON() throws JSONException {
        return new JSONObject(getString(KEY_ACTIVITY));
    }

    public void setActivity(String userID, String date) throws JSONException {
        // Sets one users activity
        JSONObject activity = getActivityJSON();
        activity.put(userID, date);
        Log.i("Group", activity.toString());
        put(KEY_ACTIVITY, activity.toString());

    }

}
