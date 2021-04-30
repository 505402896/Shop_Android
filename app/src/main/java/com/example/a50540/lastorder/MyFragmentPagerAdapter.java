package com.example.a50540.lastorder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> myListFragment;
    private List<String> myListString;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> myListFragment) {
        super(fm);
        this.myListFragment = myListFragment;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> myListFragment,List<String> myListString) {
        super(fm);
        this.myListFragment = myListFragment;
        this.myListString = myListString;
    }

    @Override
    public Fragment getItem(int position) {
        return myListFragment.get(position);
    }

    @Override
    public int getCount() {
        return myListFragment.size();
    }


}
