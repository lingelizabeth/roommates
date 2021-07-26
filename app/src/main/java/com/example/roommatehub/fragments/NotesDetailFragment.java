package com.example.roommatehub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommatehub.R;
import com.example.roommatehub.adapters.ListItemAdapter;
import com.example.roommatehub.adapters.NotesAdapter;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class NotesDetailFragment extends Fragment {

    public static final String TAG = "NotesDetailFragment";
    Note note;
    private boolean isNew;

    EditText etListTitle, etNewItem;
    Button btnAddItem, btnSave;
    RecyclerView rvListItems;
    List<ListItem> allItems;
    ListItemAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    public NotesDetailFragment() {
        // Required empty public constructor
    }

    // Notes Detail Fragment constructor with Notes
    public static NotesDetailFragment newInstance(Note note) {
        NotesDetailFragment fragmentDemo = new NotesDetailFragment();
        Bundle args = new Bundle();
        if(note.getTitle() == null){
            Log.i(TAG, "no title!!! *new note");
            args.putBoolean("isNew", true);
        } else {
            args.putBoolean("isNew", false);
        }
        args.putParcelable("note", Parcels.wrap(note));
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup title as an EditText that appears as a TextView unless clicked
        etListTitle = view.findViewById(R.id.etListTitle);
        etListTitle.setFocusableInTouchMode(true);
        etListTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Parent RelativeLayout is "focusable", so when anything else is pressed
            // this EditText loses focus and closes the keyboard.
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Save the title to this note
                note.setTitle(etListTitle.getText().toString());
                note.saveInBackground();

                // Hide Keyboard
                if(!hasFocus){
                    hideKeyboard();
                }
            }
        });

        etNewItem = view.findViewById(R.id.etNewItem);
        etNewItem.setFocusableInTouchMode(true);
        etNewItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Parent RelativeLayout is "focusable", so when anything else is pressed
            // this EditText loses focus and closes the keyboard.
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                }
            }
        });

        btnAddItem = view.findViewById(R.id.btnAddItem);

        // For new notes only, show the save button
        isNew = getArguments().getBoolean("isNew");
        if(isNew){
            btnSave = view.findViewById(R.id.btnSave);
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check for non-empty title
                    if(note.getTitle() == null){
                        Toast.makeText(getContext(), "Title can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Save new note to database
                    note.setTitle(etListTitle.getText().toString());
                    note.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error saving new note: "+e);
                                return;
                            }
                            Log.i(TAG, "New note successfully saved: "+note.getTitle());
                            // Go back to the list of fragments
                        }
                    });
                }
            });
        }

        // Unwrap note passed from Notes screen
        if(getArguments().get("note") != null) {
            note = Parcels.unwrap(getArguments().getParcelable("note"));
            Log.i("NotesDetailFragment", "Note title: "+note.getTitle());
        } else {
            Log.e(TAG, "No note specfied in NotesDetailFragment");
            return;
        }

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If a new item is typed (if not, do nothing)
                // Add that to the List in parse backend
                if(!etNewItem.getText().toString().isEmpty()){
                    ListItem newListItem = new ListItem();
                    newListItem.setText(etNewItem.getText().toString());
                    newListItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.i(TAG, "Error saving new list item: "+e);
                                return;
                            }
                            Log.i(TAG, "item save successful");
                            // Show new list item in the recycler view
                            allItems.add(newListItem);
                            adapter.notifyItemInserted(allItems.size()-1);

                            // Clear text in the new item field
                            etNewItem.setText("");

                            // add this new item to the list's itemsList for persistence
                            note.addToItemList(newListItem);
                            note.saveInBackground();
                        }
                    });
                }
            }
        });


        etListTitle.setText(note.getTitle());

        rvListItems = view.findViewById(R.id.rvListItems);

        // instantiate notes list and adapter
        allItems = new ArrayList<ListItem>();
        adapter = new ListItemAdapter(getContext(), allItems, note);

        // Set RecyclerView adapter and layout manager
        rvListItems.setAdapter(adapter);
        rvListItems.setLayoutManager(new LinearLayoutManager(getContext()));

        if(!isNew){
            populateListItems(note);


            // Lookup the swipe container view
            swipeContainer = view.findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    adapter.clear();
                    refreshNote(note.getObjectId());
                    swipeContainer.setRefreshing(false);


                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }



    }
    public void populateListItems(Note note){
        List<ListItem> items = note.getItemList();
        for(ListItem item: items){
            Log.i(TAG, "Getting list item in detail view: " +item.getText());
        }
        allItems.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public void refreshNote(String objectId){
            // specify what type of data we want to query
            ParseQuery<Note> query = ParseQuery.getQuery(Note.class);
            // include data referred by user key
            query.whereEqualTo("objectId", objectId);
            // get data for associated itemList
            query.include("itemsList");
            // start an asynchronous call for note
            query.findInBackground(new FindCallback<Note>() {
                @Override
                public void done(List<Note> notes, ParseException e) {
                    // check for errors
                    if (e != null) {
                        Log.e(TAG, "Issue refreshing notes "+e);
                        return;
                    }

                    // for debugging purposes let's print every post description to logcat
                    for (Note note : notes) {
                        Log.i(TAG, "Note: " + note.getTitle()+ " itemsList: "+note.getItemList().size());
                        if(note.getItemList().size() >= 1 ){
                            ListItem firstITem = (ListItem) note.getItemList().get(0);
                        }
                    }

                    // save received posts to list and notify adapter of new data
                    allItems.addAll(note.getItemList());
                    adapter.notifyDataSetChanged();

                    // refresh the current note in list and adapter
                    if(!notes.isEmpty()){
                         note = notes.get(0);
                         adapter.setNote(notes.get(0));
                    }
                }
            });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etListTitle.getWindowToken(), 0);
    }
}