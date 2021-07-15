package com.example.roommatehub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.example.roommatehub.adapters.NotesAdapter;
import com.example.roommatehub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment";

    RecyclerView rvNotes;
    NotesAdapter adapter;
    List<Note> allNotes;

    public NotesFragment() {
        // Required empty public constructor
    }

    // Notes Fragment constructor with Group
    public static NotesFragment newInstance(Group group) {
        NotesFragment fragmentDemo = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvNotes = view.findViewById(R.id.rvNotes);

        // instantiate notes list and adapter
        allNotes = new ArrayList<Note>();
        adapter = new NotesAdapter(getContext(), allNotes);

        // Set RecyclerView adapter and layout manager
        rvNotes.setAdapter(adapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));

//        ParseQuery<Note> query = ParseQuery.getQuery(Note.class);
//        try {
//            Note note = query.get("sVGoMSfStQ");
//
//            ParseObject soup = ParseObject.create("ListItem");
////            soup.setText("soup");
//
//            ArrayList<ParseObject> grocerylist = new ArrayList<ParseObject>();
//            grocerylist.add(soup);
//
//            note.add("itemsList", grocerylist);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        queryNotes();
    }

    // Get all notes for this group
    protected void queryNotes(){
        // specify what type of data we want to query - Post.class
        ParseQuery<Note> query = ParseQuery.getQuery(Note.class);
        // include data referred by user key
        query.include(Note.KEY_GROUP);
        query.whereEqualTo(Note.KEY_GROUP, Parcels.unwrap((Parcelable) getArguments().get("group")));
        // get data for associated itemList
        query.include("itemsList");
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Note>() {
            @Override
            public void done(List<Note> notes, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting notes", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Note note : notes) {
                    Log.i(TAG, "Note: " + note.getTitle()+ " itemsList: "+note.getItemList().size());
                    if(note.getItemList().size() >= 1 ){
                        ListItem firstITem = (ListItem) note.getItemList().get(0);
                        Log.i(TAG, "first item: "+firstITem.getText());
                    }
                }

                // save received posts to list and notify adapter of new data
                allNotes.addAll(notes);
                adapter.notifyDataSetChanged();
            }
        });
    }
}