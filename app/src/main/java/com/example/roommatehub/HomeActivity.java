package com.example.roommatehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.roommatehub.fragments.ActivityFragment;
import com.example.roommatehub.fragments.ChoresFragment;
import com.example.roommatehub.fragments.GroupProfileFragment;
import com.example.roommatehub.fragments.HomeFragment;
import com.example.roommatehub.fragments.NotesFragment;
import com.example.roommatehub.models.Group;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Unwrap parcel data for current group
        Group group = Parcels.unwrap(getIntent().getParcelableExtra("Group"));

        final FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = HomeFragment.newInstance(group);
                        break;
                    case R.id.action_chores:
                        fragment = ChoresFragment.newInstance(group);
                        break;
                    case R.id.action_notes:
                        fragment = NotesFragment.newInstance(group);
                        break;
                    case R.id.action_group:
                        fragment = GroupProfileFragment.newInstance(group);
                        break;
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }
}