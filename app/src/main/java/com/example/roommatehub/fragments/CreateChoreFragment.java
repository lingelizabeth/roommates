package com.example.roommatehub.fragments;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.OneSignalNotificationSender;
import com.example.roommatehub.R;
import com.example.roommatehub.adapters.UserIconAdapter;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.ChoreType;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.Notification;
import com.example.roommatehub.models.UserIcon;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
// ...

public class CreateChoreFragment extends DialogFragment {

    public static final String TAG = "CreateChoreFragment";

    private EditText etChore;
    private TextView tvNewChore;
    private Button btnSave, btnAdd;
    private Spinner spinnerChore, spinnerDay;

    // User profile image recyclerview setup
    private RecyclerView rvUsers;
    private UserIconAdapter adapter;
    private List<UserIcon> allUsers;

    public CreateChoreFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CreateChoreFragment newInstance(Group group) {
        CreateChoreFragment frag = new CreateChoreFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chore, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args
        Group group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Get field from view
        etChore = view.findViewById(R.id.etChore);
        tvNewChore = view.findViewById(R.id.tvNewChore);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this creates a new ChoreType, saves it to parse,
                // displays a toast, and disables the edittext so you can't create again
                ChoreType newChoreType = new ChoreType();
                String newChoreName = etChore.getText().toString();
                if(newChoreName.isEmpty()){
                    Toast.makeText(getContext(), "Chore name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                newChoreType.setName(newChoreName);
                newChoreType.setGroup(group);
                newChoreType.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Error saving new chore type: "+e);
                            return;
                        }
                        // Notify user
                        Toast.makeText(getContext(), "Created \""+newChoreName+"\" chore", Toast.LENGTH_SHORT).show();
                        // Disable so users can only add one chore type at a time
                        etChore.setEnabled(false);
                        btnAdd.setEnabled(false);
                    }
                });
            }
        });

        // Chore spinner
        spinnerChore = view.findViewById(R.id.spinnerChore);
        queryChoreTypes(group);
        // If option is other, we want to show the edittext
        spinnerChore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerChore.getItemAtPosition(position).toString().equalsIgnoreCase("Other"))
                {
                    tvNewChore.setVisibility(View.VISIBLE);
                    etChore.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Assign to users
        rvUsers = view.findViewById(R.id.rvUsers);
        allUsers = new ArrayList<>();
        adapter = new UserIconAdapter(getContext(), allUsers);
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        populateUsers(group);

        spinnerDay = view.findViewById(R.id.spinnerDay);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore chore = new Chore();
                // Get chore name from spinner
                String choreName = spinnerChore.getSelectedItem().toString();
                if(choreName.equalsIgnoreCase("Other") && !etChore.isEnabled()){
                    choreName = etChore.getText().toString();
                } else if (choreName.equalsIgnoreCase("Other") ){
                    Toast.makeText(getContext(), "Please create your new chore type by pressing the add button", Toast.LENGTH_SHORT).show();
                    return;
                }
                chore.setName(choreName);

                // Get list of assigned users from recycler view
                List<UserIcon> assignedUsers = new ArrayList<>();
                for (UserIcon userIcon : allUsers) {
                    if (userIcon.isSelected()) {
                        assignedUsers.add(userIcon);
                    }
                }
                chore.addUsersFromUserIcons(assignedUsers);

                // Set other attributes liek day of week, group, type
                chore.setDayOfWeek(spinnerDay.getSelectedItem().toString());
                chore.setGroup(group);
                chore.setChoreType(choreName, group);
                chore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "error saving chore: "+e);
                            return;
                        }
                        Log.i(TAG, "chore saved successfully");

                        // On successful save, create a notification
                        String[] data = Notification.getCreateChoreNotificationData(ParseUser.getCurrentUser(), chore, group, getString(R.string.new_chore_icon_url));
                        Notification notification = new Notification("Create chore", data,
                        "ic_bell_white_24dp",
                        "[]",
                        true,
                        group);
                        OneSignalNotificationSender.sendDeviceNotification(notification);

                        dismiss();
                    }
                });

            }
        });
    }

    public void populateUsers(Group group) {
        ParseRelation relation = group.getRelation("groupMembers");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error fetching users in group: "+e);
                    return;
                }

                List<UserIcon> userIcons = null;
                try {
                    int type = 2;
                    userIcons = UserIcon.fromUserArray(users, type);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                // Add the group members to the adapter
                allUsers.addAll(userIcons);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    public void queryChoreTypes(Group group){
        List<String> choreTypes = new ArrayList<>();
        ParseQuery query = ParseQuery.getQuery(ChoreType.class);
        query.whereEqualTo(ChoreType.KEY_GROUP, group);
        query.findInBackground(new FindCallback<ChoreType>() {
            @Override
            public void done(List<ChoreType> choreTypesList, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error fetching chore types: "+e);
                    return;
                }
                for(ChoreType choreType:choreTypesList){
                    choreTypes.add(choreType.getName());
                }

                choreTypes.add("Other");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, choreTypes);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerChore.setAdapter(adapter);

            }
        });
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Fragment fragment = getParentFragment();
        if (fragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) fragment).onDismiss(dialog);
        }
    }
}