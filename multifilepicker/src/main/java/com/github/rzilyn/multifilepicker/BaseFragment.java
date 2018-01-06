package com.github.rzilyn.multifilepicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rzilyn.multifilepicker.adapters.FileDataManager;
import com.github.rzilyn.multifilepicker.listeners.ActivityInteractionListener;
import com.github.rzilyn.multifilepicker.model.GeneralFile;
import com.github.rzilyn.multifilepicker.utils.Constant;


/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public abstract class BaseFragment extends Fragment {
    protected FileDataManager<GeneralFile> mFileDataManager;
    protected FilePickerOptions options;
    protected int[] colorScheme;
    protected ActivityInteractionListener.FragmentToActivity mFragmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null){
            this.options = args.getParcelable(Constant.EXTRA_OPTIONS);
        }
        this.colorScheme = options.getColorScheme();
        this.mFileDataManager = new FileDataManager<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ActivityInteractionListener.FragmentToActivity){
            this.mFragmentListener = (ActivityInteractionListener.FragmentToActivity) context;
        }
        else throw new IllegalStateException("Activity must implement ActivityInteractionListener");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, @LayoutRes int layoutId) {
        View view = LayoutInflater.from(getActivity()).inflate(layoutId,container,false);
        initView(view);
        setupColors();
        return view;
    }

    public abstract void initView(View view);
    public abstract void setupColors();
}
