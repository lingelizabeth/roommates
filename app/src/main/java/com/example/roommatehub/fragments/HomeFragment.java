package com.example.roommatehub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roommatehub.HomeActivity;
import com.example.roommatehub.R;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment {

    TextView tvTitle;

    public HomeFragment() {
        // Required empty public constructor
    }

    // Creates a new fragment given a title and description
    // DemoFragment.newInstance(5, "Hello");
    public static HomeFragment newInstance(String title, String description) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
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

        tvTitle = view.findViewById(R.id.tvTitle);

        // Get back arguments
        String title = getArguments().getString("title", "");
        String description = getArguments().getString("description", "");

        tvTitle.setText("Welcome to "+title+"!");
    }
}