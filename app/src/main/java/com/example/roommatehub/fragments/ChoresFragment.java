package com.example.roommatehub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roommatehub.MainActivity;
import com.example.roommatehub.R;
import com.example.roommatehub.adapters.ChorePagerAdapter;
import com.example.roommatehub.models.Group;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;


public class ChoresFragment extends Fragment {

    Group group;

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

        // get arguments
        group = Parcels.unwrap(getArguments().getParcelable("group"));

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ChorePagerAdapter(getChildFragmentManager(),
                getContext(), group));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}