package com.example.roommatehub.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.R;
import com.example.roommatehub.adapters.ChoresAdapter;
import com.example.roommatehub.adapters.UserIconAdapter;
import com.example.roommatehub.interfaces.itemFilterSelectedListener;
import com.example.roommatehub.interfaces.onCheckboxCheckedListener;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.example.roommatehub.models.UserIcon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChorePageFragment extends Fragment implements DialogInterface.OnDismissListener, itemFilterSelectedListener, onCheckboxCheckedListener {
    public static final String TAG = "ChorePageFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_DAY = "ARG_DAY";
    public static final String ARG_GROUP = "ARG_GROUP";

    private int mPage;
    private Group group;
    private String day;
    private RecyclerView rvChores, rvUsers;
    private TextView tvDay, tvProgress;
    private FloatingActionButton btnAdd;
    ProgressBar progressBar;
    private double progress; // double between 1 and 100

    private ChoresAdapter adapter;
    List<Chore> allChores;
    private UserIconAdapter userIconAdapter;
    List<UserIcon> allUserIcons;

    public static ChorePageFragment newInstance(int page, String tabName, Group group) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_DAY, tabName);
        args.putParcelable(ARG_GROUP, group);
        ChorePageFragment fragment = new ChorePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        group = getArguments().getParcelable(ARG_GROUP);
        // Set day with special cases for Wednesday and Saturday
        day = getArguments().getString(ARG_DAY).equalsIgnoreCase("Wed") ?
                day = "Wednesday": getArguments().getString(ARG_DAY).equalsIgnoreCase("Sat") ?
                day = "Saturday" : getArguments().getString(ARG_DAY)+"day";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chore_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        tvDay = view.findViewById(R.id.tv1);
//        tvDay.setText(day); // Eventually remove this

        // Recycler view setup for filter by userIcons
        rvUsers = view.findViewById(R.id.rvUsers);

        allUserIcons = new ArrayList<>();
        userIconAdapter = new UserIconAdapter(getContext(), allUserIcons, this);

        rvUsers.setAdapter(userIconAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        populateUsers(group);

        // Recycler view setup for chore list
        rvChores = view.findViewById(R.id.rvChores);

        // instantiate notes list and adapter
        allChores = new ArrayList<Chore>();
        adapter = new ChoresAdapter(getContext(), allChores, this);

        // Set RecyclerView adapter and layout manager
        rvChores.setAdapter(adapter);
        rvChores.setLayoutManager(new LinearLayoutManager(getContext()));

        queryChores(null, day);


        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog();
            }
        });

        // Set up progress bar
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tvProgress);

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
                Log.i(TAG, "Successfully fetched group members: "+users.size());

                List<UserIcon> userIcons = null;
                try {
                    int type = 1;
                    userIcons = UserIcon.fromUserArray(users, type);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                // Add the group members to the adapter
                allUserIcons.addAll(userIcons);
                userIconAdapter.notifyDataSetChanged();
            }
        });
    }


    // Query all the chores in this group containing all the pressed users for this tab/day
    private void queryChores(List<ParseUser> users, String dayOfWeek){
        ParseQuery query = ParseQuery.getQuery(Chore.class);
        // Query chores for this group
        query.whereEqualTo(Chore.KEY_GROUP, group);
        // if users is null or no one is selected, get all users for this group
        if(users != null && !users.isEmpty())
            query.whereContainsAll(Chore.KEY_USERS, users);

        // Query chores for the current tab/day of the week
        query.whereEqualTo(Chore.KEY_DAY, dayOfWeek);
        query.findInBackground(new FindCallback<Chore>() {
            @Override
            public void done(List<Chore> chores, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue fetching chores: "+e);
                }
                for(Chore chore: chores){
                    Log.i(TAG, "fetched chore: "+chore.getName());
                }
                Log.i(TAG, "Successfully fetched chores for "+day);
                allChores.addAll(chores);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void showCreateDialog() {
        FragmentManager fm = getChildFragmentManager();
        CreateChoreFragment createChoreFragment = CreateChoreFragment.newInstance(group);
        createChoreFragment.show(fm, "fragment_create_chore");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Toast.makeText(getContext(), "Dialog just dismissed", Toast.LENGTH_LONG).show();
        // Possible to pass data from the dialog fragment to here?
    }

    @Override
    public void onitemFilterSelected(List<ParseUser> selectedUsers) {
        adapter.clear();
        queryChores(selectedUsers, day);
    }

    @Override
    public void onCheckboxChecked(boolean checked) {
        // Increase or decrease progress bar accordingly
        int totalChores = allChores.size();
        double percentChange = (1.0/totalChores)*100;
        if(checked){
            progress += percentChange;
            progressBar.setProgress((int) Math.round(progress));
            tvProgress.setText(progressBar.getProgress()+"%");
        }else{
            progress -= percentChange;
            progressBar.setProgress((int) Math.round(progress));
            tvProgress.setText(progressBar.getProgress()+"%");
        }
    }
}