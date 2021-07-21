package com.example.roommatehub.models;

import com.example.roommatehub.models.Group;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("List")
public class Note extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_GROUP = "group";
    public static final String KEY_ITEMLIST = "itemsList";

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

    public List<ListItem> getItemList() {
        return getList(KEY_ITEMLIST);
    }

    public void addToItemList(ListItem newItem){
        add(KEY_ITEMLIST, newItem);
    }

    public void removeFromItemList(ListItem listItem){
        List<ListItem> removeList = new ArrayList<>();
        removeList.add(listItem);
        removeAll(KEY_ITEMLIST, removeList);
    }

    // Update object at position with new text
    public void editItemList(int position, String newText) {
        List<ListItem> items = getList(KEY_ITEMLIST);
        ListItem oldItem = items.remove(position);
        oldItem.setText(newText);
        items.add(position, oldItem);
        put(KEY_ITEMLIST, items);
    }
}
