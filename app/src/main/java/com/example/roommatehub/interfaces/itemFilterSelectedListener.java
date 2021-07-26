package com.example.roommatehub.interfaces;

import com.example.roommatehub.models.UserIcon;
import com.parse.ParseUser;

import java.util.List;

public interface itemFilterSelectedListener {
    void onitemFilterSelected(List<ParseUser> selectedUsers);

}
