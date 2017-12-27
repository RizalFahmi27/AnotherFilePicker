package com.rzilyn.github.multifilepicker.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.R;
import com.rzilyn.github.multifilepicker.adapters.FileContract;
import com.rzilyn.github.multifilepicker.listeners.FragmentInteractionListener;
import com.rzilyn.github.multifilepicker.model.GeneralFile;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilePickerSimpleFragment extends Fragment implements FragmentInteractionListener{

    private RecyclerView mRecyclerView;

    private FilePickerOptions options;
    private int[] colorScheme;

    private String TAG = getClass().getSimpleName();

    private FileContract<GeneralFile> mFileContract;

    public FilePickerSimpleFragment() {
        // Required empty public constructor
    }

    public static FilePickerSimpleFragment newInstance(){
        return new FilePickerSimpleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_picker_simple, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_file);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFileContract.getAdapter());
        registerForContextMenu(mRecyclerView);
        return view;
    }


    @Override
    public void setFileContract(@NonNull FileContract<GeneralFile> fileContract) {
        this.mFileContract = fileContract;
    }

    @Override
    public void setFileOptions(@NonNull FilePickerOptions fileOptions) {
        this.options = fileOptions;
    }
}
