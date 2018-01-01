package com.rzilyn.github.multifilepicker.adapters;

import android.support.design.widget.TabItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public class FragmentSectionAdapter extends FragmentPagerAdapter{
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitle = new ArrayList<>();

    public FragmentSectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentTitle.add(title);
        mFragments.add(fragment);
    }

    public void removeFragment(int position){
        mFragmentTitle.remove(position);
        mFragments.remove(position);
        notifyDataSetChanged();
    }
}
