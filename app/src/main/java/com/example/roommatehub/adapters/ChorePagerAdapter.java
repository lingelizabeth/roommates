package com.example.roommatehub.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.roommatehub.fragments.ChorePageFragment;
import com.example.roommatehub.models.Group;

public class ChorePagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 7;
    private String tabTitles[] = new String[] { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
    private Context context;
    private Group group;

    public ChorePagerAdapter(FragmentManager fm, Context context, Group group) {
        super(fm);
        this.context = context;
        this.group = group;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return ChorePageFragment.newInstance(position + 1, ""+getPageTitle(position), group);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}