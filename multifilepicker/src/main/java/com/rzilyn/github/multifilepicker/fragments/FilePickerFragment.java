package com.rzilyn.github.multifilepicker.fragments;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rzilyn.github.multifilepicker.BaseFragment;
import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.MultiFilePicker;
import com.rzilyn.github.multifilepicker.R;
import com.rzilyn.github.multifilepicker.adapters.FileContract;
import com.rzilyn.github.multifilepicker.adapters.SimpleFileAdapter;
import com.rzilyn.github.multifilepicker.listeners.ActivityInteractionListener;
import com.rzilyn.github.multifilepicker.listeners.BaseAdapterListener;
import com.rzilyn.github.multifilepicker.listeners.FragmentInteractionListener;
import com.rzilyn.github.multifilepicker.loader.ScanResultCallback;
import com.rzilyn.github.multifilepicker.model.GeneralFile;
import com.rzilyn.github.multifilepicker.utils.ActivityUtil;
import com.rzilyn.github.multifilepicker.utils.Constant;
import com.rzilyn.github.multifilepicker.utils.FileLoaderHelper;
import com.rzilyn.github.multifilepicker.utils.FileUpdateMethod;
import com.rzilyn.github.multifilepicker.utils.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilePickerFragment extends BaseFragment implements BaseAdapterListener<GeneralFile>, View.OnClickListener,
        ActivityInteractionListener.ActivityToFragment, FragmentInteractionListener {

    private FloatingActionButton mFAB;
    private View mContainerLoading;
    private FrameLayout mContainerFragment;
    private ProgressBar mProgressBar;
    private Snackbar mSnackbar;

    public FilePickerFragment() {
        // Required empty public constructor
    }

    public static FilePickerFragment newInstance(FilePickerOptions options){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_OPTIONS,options);
        FilePickerFragment fragment = new FilePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView(View view) {
        mContainerLoading = view.findViewById(R.id.container_loadingView);
        mContainerFragment = view.findViewById(R.id.container_childFragment);
        mFAB = view.findViewById(R.id.fab_done);
        mFAB.setOnClickListener(this);
        mProgressBar = view.findViewById(R.id.progressBar);
        mSnackbar = Snackbar.make(view,getString(R.string.text_loading),Snackbar.LENGTH_INDEFINITE);

        this.mFileDataManager.addAdapter(new SimpleFileAdapter(new ArrayList<GeneralFile>(0),
                getActivity(),this,colorScheme, !options.isSinglePick()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater,container,savedInstanceState, R.layout.fragment_file_picker);

        Fragment fragment = null;

        // TODO prevent from illegalstate casting
        if(options.isTabEnabled()){
            fragment = FilePickerTabbedFragment.newInstance();
        }
        else fragment = FilePickerSimpleFragment.newInstance();
        ActivityUtil.addFragment(getChildFragmentManager(),fragment,R.id.container_childFragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFiles();
    }

    @Override
    public boolean onItemSelected(GeneralFile file) {
        int selectedFileCount = mFileDataManager.getSelectedFile().size();
        if(selectedFileCount >= options.getFileLimit())
            return false;

        mFileDataManager.addSelectedFile(file.getId(),file);

        if(options.isSinglePick())
            returnResult();

        selectedFileCount = mFileDataManager.getSelectedFile().size();
        if(!options.isSinglePick() && selectedFileCount > 0) {
            mFAB.show();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Selected file ("+selectedFileCount+"/"+options.getFileLimit()+")");
        }
        return true;
    }

    @Override
    public boolean onItemUnselected(GeneralFile file) {
        mFileDataManager.removeSelectedFile(file.getId());
        int selectedFileCount = mFileDataManager.getSelectedFile().size();
        if(!options.isSinglePick()) {
            if(selectedFileCount < 1) {
                mFAB.hide();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(options.getHint());
            }
            else ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Selected file ("+selectedFileCount+"/"+options.getFileLimit()+")");
        }
        return true;
    }

    @Override
    public boolean isFileSelected(GeneralFile file) {
        return mFileDataManager.isFileSelected(file.getId());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = item.getItemId();
        if (i == R.id.action_open) {
            GeneralFile file = ((SimpleFileAdapter)mFileDataManager.getAdapter()).getCurrentItem();

            String mimeType = file.getMimeType();
            if(mimeType == null){
                Toast.makeText(getActivity(),"File type not supported",Toast.LENGTH_SHORT).show();
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(new File(file.getFilepath()));
            intent.setDataAndType(data,mimeType);
            startActivity(intent);
            return true;
        }
        else if(i == R.id.action_select){
            GeneralFile file = ((SimpleFileAdapter)mFileDataManager.getAdapter()).getCurrentItem();
            onItemSelected(file);
            return true;
        }
        else return super.onContextItemSelected(item);
    }

    private void getFiles() {
        switchLoadingState(true);
        FileLoaderHelper.getAllfiles(getActivity(), options, new ScanResultCallback() {
            @Override
            public void onFileScanFinished(List<GeneralFile> fileList) {
                if(options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                    if(options.isTabEnabled())
                    mFileDataManager.replaceAndSetData(fileList,options.getFileFilters().get(0),mFragmentListener.getSortType(),
                            mFragmentListener.getSortOrder(),mFragmentListener.getSearchQuery());
                    else mFileDataManager.replaceData(fileList);
                }
                mFileDataManager.setHasfinishedLoading(true);
                switchLoadingState(false);
            }

            @Override
            public void onFileScanUpdate(GeneralFile file) {
                mFileDataManager.addData(file);
                // Filter file when search view is active
                // If the emitting file fulfill the query pattern, then add it to adapter
                if(mFragmentListener.isSearchVisible())
                    mFileDataManager.filterFile(mFragmentListener.getSearchQuery(),true);
                else mFileDataManager.notifyDataChange();
            }
        });
    }

    private void switchLoadingState(boolean isLoading){
        if(isLoading) {
            if (options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                mContainerLoading.setVisibility(View.VISIBLE);
                mContainerFragment.setVisibility(View.GONE);
            } else if (options.getUpdateMethod() == FileUpdateMethod.STREAM) {
                mContainerLoading.setVisibility(View.GONE);
                mContainerFragment.setVisibility(View.VISIBLE);
                mSnackbar.show();
            }
        }
        else {
            if(options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                mContainerLoading.setVisibility(View.GONE);
                mContainerFragment.setVisibility(View.VISIBLE);
            }
            else if(options.getUpdateMethod() == FileUpdateMethod.STREAM){
                mSnackbar.dismiss();
            }
        }
    }

    private void returnResult(){
        ArrayList<String> results = new ArrayList<>();
        for (Map.Entry<String, GeneralFile> selectedFile : mFileDataManager.getSelectedFile().entrySet()){
            String path = selectedFile.getValue().getFilepath();
            results.add(path);
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(MultiFilePicker.RESULT_FILE,results);
        getActivity().setResult(RESULT_OK,intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if( i == R.id.fab_done){
            returnResult();
        }
    }

    @Override
    public void setupColors() {
        if(colorScheme.length > 2) {
            mProgressBar.getIndeterminateDrawable().setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            ((TextView)mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(colorScheme[2]);
        }
        else mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void filter(String query) {
        if(mFileDataManager.hasFinishedLoading()) {
            mFileDataManager.filterFile(query,true);
        }
    }

    @Override
    public void sort(Sort.Type type, Sort.Order order) {
        mFileDataManager.sortFile(type,order,true);
    }

    @Override
    public void recoverOriginalData() {
        mFileDataManager.recoverOriginalData();
    }

    @Override
    public FileContract<GeneralFile> getFileContract() {
        return mFileDataManager;
    }

    @Override
    public FilePickerOptions getFileOptions() {
        return options;
    }
}
