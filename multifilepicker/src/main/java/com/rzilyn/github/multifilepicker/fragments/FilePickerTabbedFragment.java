package com.rzilyn.github.multifilepicker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rzilyn.github.multifilepicker.FilePickerActivity;
import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.R;
import com.rzilyn.github.multifilepicker.adapters.FileContract;
import com.rzilyn.github.multifilepicker.adapters.FragmentSectionAdapter;
import com.rzilyn.github.multifilepicker.adapters.SimpleFileAdapter;
import com.rzilyn.github.multifilepicker.listeners.FragmentInteractionListener;
import com.rzilyn.github.multifilepicker.model.GeneralFile;
import com.rzilyn.github.multifilepicker.utils.Sort;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilePickerTabbedFragment extends Fragment implements FragmentInteractionListener{

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private FragmentSectionAdapter mPagerAdapter;
    private FragmentInteractionListener mFragmentInteractionListener;

    public FilePickerTabbedFragment() {
        // Required empty public constructor
    }

    public static FilePickerTabbedFragment newInstance(){
        return new FilePickerTabbedFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getParentFragment() instanceof FragmentInteractionListener){
            mFragmentInteractionListener = (FragmentInteractionListener) getParentFragment();
        }
        else throw new IllegalStateException("Fragment must implement FragmentInteractionListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_file_picker_tabbed, container, false);
        mViewPager = view.findViewById(R.id.viewPager_main);
        mTablayout = view.findViewById(R.id.tablayout_main);

        setupViewPager();

        return view;
    }

    private void setData(String projection){
        Sort.Type sortType = ((FilePickerActivity)getActivity()).getSortType();
        Sort.Order sortOrder = ((FilePickerActivity)getActivity()).getSortOrder();
        String searchQuery = ((FilePickerActivity)getActivity()).getSearchQuery();
        mFragmentInteractionListener.getFileContract().setData(projection,sortType,sortOrder,searchQuery);
    }

    private void setupViewPager() {
        mPagerAdapter = new FragmentSectionAdapter(getChildFragmentManager());
        final List<String> supportedFileType = mFragmentInteractionListener.getFileOptions().getFileFilters();
        for(String s : supportedFileType){
            FilePickerSimpleFragment fragment = FilePickerSimpleFragment.newInstance(s);
            mPagerAdapter.addFragment(fragment,s);
        }
        ((SimpleFileAdapter)mFragmentInteractionListener.getFileContract().
                getAdapter()).setProjection(supportedFileType.get(0));
        mViewPager.setOffscreenPageLimit(supportedFileType.size());
        mViewPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        setData(supportedFileType.get(0));
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setData(supportedFileType.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void removeTab(int position){
        if(mTablayout.getTabCount() >= 1 && position<mTablayout.getTabCount()){
            mTablayout.removeTabAt(position);
            mPagerAdapter.removeFragment(position);
        }
    }

    public void addTab(String title, Fragment fragment){
        mTablayout.addTab(mTablayout.newTab().setText(title));
        mPagerAdapter.addFragment(fragment,title);
    }

    @Override
    public FileContract<GeneralFile> getFileContract() {
        return mFragmentInteractionListener.getFileContract();
    }

    @Override
    public FilePickerOptions getFileOptions() {
        return mFragmentInteractionListener.getFileOptions();
    }
}
