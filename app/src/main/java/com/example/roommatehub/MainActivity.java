package com.example.roommatehub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roommatehub.adapters.GroupsAdapter;
import com.example.roommatehub.models.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 143;

    Button btnLogout, btnCreateGroup;
    RecyclerView rvGroups;
    GroupsAdapter adapter;
    List<Group> allGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutButton();
            }
        });

        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        rvGroups = findViewById(R.id.rvGroups);

        // Initialize list and adapter
        allGroups = new ArrayList<>();
        adapter = new GroupsAdapter(this, allGroups);

        // Set recyclerview adapter and layout manager
        rvGroups.setAdapter(adapter);
        rvGroups.setLayoutManager(new LinearLayoutManager(this));

        queryGroups();
    }

    private void queryGroups() {
        // Given a user object
        ParseUser me = ParseUser.getCurrentUser();
        // first we will create a query on the Group object
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
        // now we will query the authors relation to see if the author object we have
        // is contained therein
        query.whereEqualTo("groupMembers", me);

        query.setLimit(20);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Group group : groups) {
                    Log.i(TAG, "Group name: " + group.getTitle());
                }

                // save received posts to list and notify adapter of new data
                allGroups.addAll(groups);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Called once the create activity completes; inserts new group at the top of groups list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            // Get data (new tweet) from intent
            Group group = Parcels.unwrap(data.getParcelableExtra("New group"));
            // Update recycler view with this new tweet and update display
            allGroups.add(0, group);
            adapter.notifyItemInserted(0);
            rvGroups.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLogoutButton(){
        Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT);
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // set current user to null

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

}