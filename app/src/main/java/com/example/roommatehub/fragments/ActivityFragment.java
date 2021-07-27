package com.example.roommatehub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roommatehub.R;
import com.example.roommatehub.adapters.NotificationsAdapter;
import com.example.roommatehub.adapters.UserAdapter;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.Notification;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    public static final String TAG = "ActivityFragment";

    Group group;
    RecyclerView rvNotifications;
    List<Notification> allNotifications;
    NotificationsAdapter adapter;

    public ActivityFragment() {
        // Required empty public constructor
    }

    // Creates a new fragment given a Group
    public static ActivityFragment newInstance(Group group) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args
        group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Set up recycler view
        rvNotifications = view.findViewById(R.id.rvNotifications);
        allNotifications = new ArrayList<>();
        adapter = new NotificationsAdapter(getContext(), allNotifications);
        rvNotifications.setAdapter(adapter);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
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
            }
        });
    }
}