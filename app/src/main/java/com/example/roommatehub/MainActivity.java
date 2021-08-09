package com.example.roommatehub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommatehub.adapters.GroupsAdapter;
import com.example.roommatehub.models.Group;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 143;
    private static final int REQUEST_FINE_LOCATION = 1;

    private FusedLocationProviderClient fusedLocationClient;

    TextView tvHello;
    Button btnLogout;
    FloatingActionButton btnCreateGroup;
    RecyclerView rvGroups;
    GroupsAdapter adapter;
    List<Group> allGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if not signed in, go to login
        Log.i(TAG, "current user: "+ParseUser.getCurrentUser());
        if(ParseUser.getCurrentUser() == null){
            goSplashActivity();
        }

        // Update this user's last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        tvHello = findViewById(R.id.tvHello);
        tvHello.setText("Hello, "+Helper.toTitleCase((String) ParseUser.getCurrentUser().get("firstName"))+"!");

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
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
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
                    Log.e(TAG, "Issue with getting groups", e);
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

        Intent i = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(i);
    }


    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.i(TAG, msg);
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)

        checkPermissions();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                Log.i(TAG, "get location success! " + location.toString());
                                onLocationChanged(location);

                                // Save location to parse database
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                                user.put("locationUpdatedTime", new Date()); // Automatically instantiates current time
                                user.saveInBackground();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    // Intent to Splash Activity
    private void goSplashActivity() {
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        finish();
    }

}