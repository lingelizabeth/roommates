package com.example.roommatehub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommatehub.Helper;
import com.example.roommatehub.MainActivity;
import com.example.roommatehub.R;
import com.example.roommatehub.adapters.ChorePagerAdapter;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;


public class ChoresFragment extends Fragment {

    public static final String TAG = "ChoresFragment";

    Group group;
    TextView tvWeek;
    Button btnClear;
    ViewPager viewPager;
    TabLayout tabLayout;

    public ChoresFragment() {
        // Required empty public constructor
    }

    // Constructor with group
    public static ChoresFragment newInstance(Group group) {
        ChoresFragment fragment = new ChoresFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", Parcels.wrap(group));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Week textview
        tvWeek = view.findViewById(R.id.tvWeek);
        tvWeek.setText(Helper.getCurrentWeekString());

        // Clear button
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Query all the chores in this group
                // Set complete to false
                // Save chores
                clearChoreProgress();
                Toast.makeText(getContext(), "Progress cleared, please refresh the tab!", Toast.LENGTH_LONG).show();
            }
        });

        // get arguments
        group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ChorePagerAdapter(getChildFragmentManager(),
                getContext(), group));

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Query all the chores in this group and clear their progress
    private void clearChoreProgress(){
        ParseQuery query = ParseQuery.getQuery(Chore.class);
        // Query chores for this group
        query.whereEqualTo(Chore.KEY_GROUP, group);
        // Pass in a list of parse users to do filtering
        // if users is null or no one is selected, get all users for this group
//        if(users != null && !users.isEmpty())
//            query.whereContainsAll(Chore.KEY_USERS, users);
        query.findInBackground(new FindCallback<Chore>() {
            @Override
            public void done(List<Chore> chores, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue fetching chores: "+e);
                }
                Log.i(TAG, "Successfully fetched chores");
                for(Chore chore: chores){
                    //Set complete to false
                    chore.setChecked(false);
                    chore.saveInBackground();
                }
            }
        });
    }
}