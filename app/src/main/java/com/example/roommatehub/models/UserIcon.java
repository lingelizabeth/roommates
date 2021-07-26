package com.example.roommatehub.models;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserIcon {
    private String imageUrl;
    private ParseUser user;
    private boolean isSelected = false;
    private static String default_profile_image_url = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
    public int ITEM_TYPE; // 0, 1, or 2

    public UserIcon(ParseUser user, String imageUrl, int type) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.ITEM_TYPE = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public static List<UserIcon> fromUserArray(List<ParseUser> users, int type) throws ParseException {
        List<UserIcon> userIcons = new ArrayList<>();
        for(ParseUser user: users){
            String url = user.fetchIfNeeded().getParseFile("profileImage") == null ?
                    default_profile_image_url :
                    user.getParseFile("profileImage").getUrl();
            userIcons.add(new UserIcon(user, url, type));
        }
        return userIcons;
    }

    public static List<ParseUser> getSelectedUsers(List<UserIcon> userIcons){
        List<ParseUser> selectedUsers = new ArrayList<>();
        for(UserIcon userIcon:userIcons){
            if(userIcon.isSelected){
                selectedUsers.add(userIcon.getUser());
            }
        }
        return selectedUsers;
    }

}
