package com.example.roommatehub.fragments;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.roommatehub.Helper;
import com.example.roommatehub.HomeActivity;
import com.example.roommatehub.OneSignalNotificationSender;
import com.example.roommatehub.R;
import com.example.roommatehub.adapters.NotificationsAdapter;
import com.example.roommatehub.adapters.UserIconAdapter;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.Notification;
import com.example.roommatehub.models.UserIcon;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    TextView tvTitle;
    RelativeLayout choreWidget;
    Button btnGoToGroupProfile;
    RecyclerView rvNotifications, rvMembers;
    List<Notification> allNotifications;
    NotificationsAdapter adapter;
    List<UserIcon> allMembers;
    UserIconAdapter userIconAdapter;

    ProgressBar progressBar, pb;
    TextView tvProgress;

    public HomeFragment() {
        // Required empty public constructor
    }

    // Creates a new fragment given a Group
    public static HomeFragment newInstance(Group group) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get back arguments
        Group group = Parcels.unwrap(getArguments().getParcelable("group"));
        String title = group.getTitle();

        // Initialize views
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("Welcome to "+title+"!");

        // Group Members section and recyclerview
        btnGoToGroupProfile = view.findViewById(R.id.btnGoToGroupProfile);
        btnGoToGroupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goGroupProfile(v, group);
            }
        });
        rvMembers = view.findViewById(R.id.rvMembers);
        allMembers = new ArrayList<>();
        userIconAdapter = new UserIconAdapter(getContext(), allMembers);
        rvMembers.setAdapter(userIconAdapter);
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Chore Widget
        choreWidget = view.findViewById(R.id.choreWidget);
        choreWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goChoresFragment(v, group);
            }
        });
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        // full name form of the day
        String dayOfWeek = (new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));
        int progress = Helper.getProgressOnDay(dayOfWeek, group);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
        tvProgress = view.findViewById(R.id.tvProgress);
        tvProgress.setText(progress+"%");

        // Set up recycler view
        JSONObject memberActivity = null;
        try {
            memberActivity = group.getActivityJSON();
            String dateString = (String) memberActivity.get(ParseUser.getCurrentUser().getObjectId());
            Log.i(TAG, "user's date: "+dateString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rvNotifications = view.findViewById(R.id.rvNotifications);
        allNotifications = new ArrayList<>();
        adapter = new NotificationsAdapter(getContext(), allNotifications);
        rvNotifications.setAdapter(adapter);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show indeterminate loading icon while fetching notifications
        pb = view.findViewById(R.id.pbLoading);
        pb.setVisibility(View.VISIBLE);
        populateUsers(group);
        populateNotifs(group);
    }

    public void populateNotifs(Group group){
        ParseQuery query = new ParseQuery(Notification.class);
        query.whereEqualTo(Notification.KEY_GROUP, group);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Notification>() {
            @Override
            public void done(List<Notification> notifs, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error fetching users in group: " + e);
                    return;
                }

                for (Notification notif : notifs) {
                    notif.parseContent();
                    Log.i(TAG, "got group notif: " + notif.getTitle());
                }

                // Add the group members to the adapter
                allNotifications.addAll(notifs);
                adapter.notifyDataSetChanged();

                pb.setVisibility(View.INVISIBLE);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Save the current time after 5s
                        Log.i(TAG, "recycler view finished populating");
                        // Save the time into the current user's activity
                        Date date = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = dateFormat.format(date);
                        try {
                            group.setActivity(ParseUser.getCurrentUser().getObjectId(), strDate);
                            group.saveInBackground();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        Log.i(TAG, "saved current time");
                    }
                }, 5000);
            }
        });
    }

    private void populateUsers(Group group) {
        ParseRelation relation = group.getRelation("groupMembers");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error fetching users in group: "+e);
                    return;
                }
                Log.i(TAG, "Successfully fetched "+users.size()+" group members");

                List<UserIcon> userIcons = null;
                try {
                    int type = 3; // Custom ViewHolder for Member Activity
                    userIcons = UserIcon.fromUserArray(users, type);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                // Add the group members to the adapter
                allMembers.addAll(userIcons);
                userIconAdapter.notifyDataSetChanged();
            }
        });
    }

    public void goGroupProfile(View v, Group group){
        // Go to Group Profile fragment
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = GroupProfileFragment.newInstance(group);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, myFragment)
                .addToBackStack(null)
                .commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_group);

    }
    public void goChoresFragment(View v, Group group){
        // Go to chores fragment
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = ChoresFragment.newInstance(group);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, myFragment)
                .addToBackStack(null)
                .commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_chores);

    }
}