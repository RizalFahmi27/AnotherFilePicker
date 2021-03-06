package com.github.rzilyn.multifilepicker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rzilyn.multifilepicker.R;
import com.github.rzilyn.multifilepicker.adapters.SimpleFileAdapter;
import com.github.rzilyn.multifilepicker.interfaces.FragmentInteractionListener;
import com.github.rzilyn.multifilepicker.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilePickerSimpleFragment extends Fragment{

    private RecyclerView mRecyclerView;

    private String TAG = getClass().getSimpleName();

    private FragmentInteractionListener mFragmentInteractionListener;

    public FilePickerSimpleFragment() {
        // Required empty public constructor
    }


    public static FilePickerSimpleFragment newInstance(){
        return new FilePickerSimpleFragment();
    }

    public static FilePickerSimpleFragment newInstance(int position){
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_ADAPTER_POSITION,position);
        FilePickerSimpleFragment fragment = new FilePickerSimpleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getParentFragment() instanceof FragmentInteractionListener){
            this.mFragmentInteractionListener = (FragmentInteractionListener) getParentFragment();
        }
        else throw new IllegalStateException("Fragment must implement FragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mFragmentInteractionListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_picker_simple, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_file);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int adapterPos = 0;
        if(getArguments()!=null) {
            adapterPos = (getArguments().getInt(Constant.EXTRA_ADAPTER_POSITION));
        }
        SimpleFileAdapter adapter = (SimpleFileAdapter) mFragmentInteractionListener.getFileContract().getAdapter(adapterPos);
        mRecyclerView.setAdapter(adapter);
        registerForContextMenu(mRecyclerView);
        return view;
    }
}
