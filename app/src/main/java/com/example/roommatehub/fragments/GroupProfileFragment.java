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
import android.widget.TextView;

import com.example.roommatehub.R;
import com.example.roommatehub.adapters.UserAdapter;
import com.example.roommatehub.models.Group;
import com.google.android.gms.location.LocationRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class GroupProfileFragment extends Fragment {

    public static final String TAG = "GroupProfileFragment";

    private Group group;
    TextView tvName, tvDescription;
    RecyclerView rvUsers;
    List<ParseUser> allUsers;
    UserAdapter adapter;

    public GroupProfileFragment() {
        // Required empty public constructor
    }

    public static GroupProfileFragment newInstance(Group group) {
        GroupProfileFragment frag = new GroupProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get args
        group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Initialize views
        tvName = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvName.setText(group.getTitle());
        tvDescription.setText(group.getDescription());

        // Set up recyclerview
        rvUsers = view.findViewById(R.id.rvUsers);
        allUsers = new ArrayList<>();
        adapter = new UserAdapter(getContext(), allUsers, group.getAddress());
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        populateUsers(group);
    }

    // ** this is very similar code to create chore fragment!
    public void populateUsers(Group group) {
        ParseRelation relation = group.getRelation("groupMembers");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error fetching users in group: " + e);
                    return;
                }

                for (ParseUser user : users) {
                    Log.i(TAG, "group user: " + user.getUsername());
                }

                // Add the group members to the adapter
                allUsers.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });
    }

}