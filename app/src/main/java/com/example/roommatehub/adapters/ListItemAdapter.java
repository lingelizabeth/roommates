package com.example.roommatehub.adapters;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.roommatehub.R;
import com.example.roommatehub.models.ListItem;
import com.example.roommatehub.models.Note;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    public static final String TAG = "ListItemAdapter";

    private Context context;
    private List<ListItem> listItems;
    private Note note;
    private boolean noteChanged = false;

    public ListItemAdapter(Context context, List<ListItem> listItems, Note note) {
        this.context = context;
        this.listItems = listItems;
        this.note = note;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.bind(listItem);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        listItems.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ListItem> list) {
        listItems.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        EditText etText;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            etText = itemView.findViewById(R.id.etText);
            // Set edittext to disabled
            etText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                // Parent RelativeLayout is "focusable", so when anything else is pressed
                // this EditText loses focus and closes the keyboard.
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION) {
                        // Stretch goal: Add code here to check server and if there are remote changes
                        // Save the edited item text to the note
                        note.editItemList(position, etText.getText().toString());
                        note.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    Log.e(TAG, "Error saving edited list item: "+e);
                                }
                                // Clear the current user's currently editing status
                                try {
                                    note.getItemList().get(position).fetchIfNeeded().remove("currentlyEditing");
                                } catch (ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            }
                        });


                        // Hide Keyboard
                        if (!hasFocus) {
                            hideKeyboard();
                            etText.setFocusable(false);

                        }
                    }
                }
            });
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION) {
                        // Run another query to get the most updated currently editing
                        refreshNote();
                        if(noteChanged){
                            Snackbar.make(itemView, "Please refresh before you can edit!", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        String name = note.getItemList().get(position).getCurrentlyEditingUsername();
                        if(name == ""){
                            // if no other is editing, set editor to current user
                            note.getItemList().get(position).setCurrentlyEditing(ParseUser.getCurrentUser());
                            note.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(context, "just saved currently editing to parse! from"+ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "just saved currently editing to parse! from"+ParseUser.getCurrentUser().getUsername());
                                }
                            });
                            etText.setFocusableInTouchMode(true);
                            etText.requestFocus();
                            showKeyboard();
                        } else if(!name.equals(ParseUser.getCurrentUser().getUsername())) {
                            // if someone else is editing, display a message
                            Snackbar.make(itemView, "Someone else is editing this item!", Snackbar.LENGTH_LONG)
                                    .show();
                        }else{
                            // I am currently editing
                            etText.setFocusableInTouchMode(true);
                        }
                        Log.i(TAG, "currently editing vs current user's usernames: "+name+" "+ParseUser.getCurrentUser().getUsername());
                    }

                }
            });
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete this list item and remove from adapter
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        ListItem removedItem = listItems.remove(position);
                        notifyItemRemoved(position);
                        note.removeFromItemList(removedItem);
                        note.saveInBackground();
                    }
                }
            });
            itemView.setOnLongClickListener(this);
        }

        public void bind(ListItem listItem){
            etText.setText(listItem.getText());
        }



        @Override
        public boolean onLongClick(View v) {
            Log.i(TAG, "long press on "+ etText.getText());
            // Show buttons for 3 seconds, then set to invisible
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.postDelayed(new Runnable() {
                public void run() {
                    btnEdit.setVisibility(View.INVISIBLE);
                }
            }, 3000);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.postDelayed(new Runnable() {
                public void run() {
                    btnDelete.setVisibility(View.INVISIBLE);
                }
            }, 3000);
            return false;
        }

        private void showKeyboard(){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etText.getWindowToken(), 0);
        }

        private void refreshNote(){
            // Directly edits and updates the note for this list item
            // specify what type of data we want to query
            // save original note data to check if it changes later
            noteChanged = false;
            List<String> originalText = new ArrayList<>();
            for(ListItem l : note.getItemList()){
                originalText.add(l.getText());
            }

            ParseQuery<Note> query = ParseQuery.getQuery(Note.class);
            query.whereEqualTo("objectId", note.getObjectId());
            query.include(Note.KEY_ITEMLIST);
            // TODO: make sure you can't edit if the note has been changed
            query.findInBackground(new FindCallback<Note>() {
                @Override
                public void done(List<Note> notes, ParseException e) {
                    // check for errors
                    if (e != null) {
                        Log.e(TAG, "Issue fetching most recent notes: "+e);
                        return;
                    }
                    // refresh the current note
                    if(!notes.isEmpty()) {
                        note = notes.get(0);
                        for(int i=0;i< note.getItemList().size();i++){
                            ListItem temp = new ListItem();
                            try {
                                temp = note.getItemList().get(i).fetchIfNeeded();
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                            if(!originalText.get(i).equals(temp.getText())){
                                // if refreshed text has changed, prompt user to refresh
//                                Snackbar.make(itemView, "Please refresh before you can edit!", Snackbar.LENGTH_LONG).show();
                                noteChanged = true;
                            }
                        }
                    }
                }
            });
        }

    }
}
