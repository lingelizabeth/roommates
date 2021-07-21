package com.example.roommatehub.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatehub.R;
import com.example.roommatehub.adapters.ChoresAdapter;
import com.example.roommatehub.models.Chore;
import com.example.roommatehub.models.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class ChorePageFragment extends Fragment {
    public static final String TAG = "ChorePageFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_DAY = "ARG_DAY";
    public static final String ARG_GROUP = "ARG_GROUP";

    private int mPage;
    private Group group;
    private String day;
    private RecyclerView rvChores;
    private TextView tvDay;
    private ChoresAdapter adapter;
    private FloatingActionButton btnAdd;
    List<Chore> allChores;

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
        day = getArguments().getString(ARG_DAY)+"day";
        group = getArguments().getParcelable(ARG_GROUP);
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

        tvDay = view.findViewById(R.id.tv1);
        tvDay.setText(day); // Eventually remove this
        rvChores = view.findViewById(R.id.rvChores);

        // instantiate notes list and adapter
        allChores = new ArrayList<Chore>();
        adapter = new ChoresAdapter(getContext(), allChores);

        // Set RecyclerView adapter and layout manager
        rvChores.setAdapter(adapter);
        rvChores.setLayoutManager(new LinearLayoutManager(getContext()));

        queryChores(day);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog();
            }
        });
    }

    private void queryChores(String dayOfWeek){
        ParseQuery query = ParseQuery.getQuery(Chore.class);
        // Only chores for this group
        Log.i(TAG, "group id: "+group.getObjectId());
        query.whereEqualTo(Chore.KEY_GROUP, group);
        // Only chores for this day of the week
        query.whereEqualTo(Chore.KEY_DAY, day);
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
}
