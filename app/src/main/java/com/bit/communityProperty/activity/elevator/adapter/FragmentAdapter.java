package com.bit.communityProperty.activity.elevator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class FragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> listFragments;
    private String[] tabTitleArray;

    public FragmentAdapter(FragmentManager fm, List<Fragment> al, String[] tabTitleArray) {
        super(fm);
        this.tabTitleArray = tabTitleArray;
        listFragments = al;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleArray[position % tabTitleArray.length];
    }
}
