package com.github.rzilyn.multifilepicker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rzilyn.multifilepicker.FilePickerActivity;
import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.R;
import com.github.rzilyn.multifilepicker.adapters.FileContract;
import com.github.rzilyn.multifilepicker.adapters.FragmentSectionAdapter;
import com.github.rzilyn.multifilepicker.listeners.FragmentInteractionListener;
import com.github.rzilyn.multifilepicker.model.GeneralFile;
import com.github.rzilyn.multifilepicker.utils.Sort;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilePickerTabbedFragment extends Fragment implements FragmentInteractionListener {

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

        FilePickerOptions options = mFragmentInteractionListener.getFileOptions();
        if(options.getColorScheme().length > 0)
            mTablayout.setBackgroundColor(options.getColorScheme()[0]);
        int selectedTextColor;
        int normalTextColor = mTablayout.getTabTextColors().getColorForState(new int[]{-android.R.attr.state_selected}, ContextCompat.getColor(getActivity(),R.color.colorWhite));

        if(options.getColorScheme().length > 1)
            normalTextColor = options.getColorScheme()[1];
        if(options.getColorScheme().length > 2)
            selectedTextColor = options.getColorScheme()[2];
        else selectedTextColor = normalTextColor;

        mTablayout.setTabTextColors(normalTextColor,selectedTextColor);
        mTablayout.setSelectedTabIndicatorColor(selectedTextColor);

        setupViewPager();

        return view;
    }


    private void setupViewPager() {
        mPagerAdapter = new FragmentSectionAdapter(getChildFragmentManager());
        final List<String> supportedFileType = mFragmentInteractionListener.getFileOptions().getFileFilters();
        for(int i=0;i<supportedFileType.size();i++){
            FilePickerSimpleFragment fragment = FilePickerSimpleFragment.newInstance(i);
            mPagerAdapter.addFragment(fragment,supportedFileType.get(i));
        }
        mViewPager.setOffscreenPageLimit(supportedFileType.size());
        mViewPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((FilePickerActivity)getActivity()).setActiveTab(tab.getPosition());
                Sort.Type sortType = ((FilePickerActivity)getActivity()).getSortType();
                Sort.Order sortOrder = ((FilePickerActivity)getActivity()).getSortOrder();
                String query = ((FilePickerActivity)getActivity()).getSearchQuery();
                mFragmentInteractionListener.getFileContract().setData(supportedFileType.get(tab.getPosition()),
                        sortType,sortOrder,query,tab.getPosition());
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
