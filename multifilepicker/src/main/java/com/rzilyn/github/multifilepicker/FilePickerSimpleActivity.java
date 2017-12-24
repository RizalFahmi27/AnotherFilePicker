package com.rzilyn.github.multifilepicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rzilyn.github.multifilepicker.adapter.FileDataManager;
import com.rzilyn.github.multifilepicker.adapter.SimpleFileAdapter;
import com.rzilyn.github.multifilepicker.loader.ScanResultCallback;
import com.rzilyn.github.multifilepicker.model.GeneralFile;
import com.rzilyn.github.multifilepicker.utils.BaseAdapterListener;
import com.rzilyn.github.multifilepicker.utils.Constant;
import com.rzilyn.github.multifilepicker.utils.FileLoaderHelper;
import com.rzilyn.github.multifilepicker.utils.FileUpdateMethod;
import com.rzilyn.github.multifilepicker.utils.Sort;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FilePickerSimpleActivity extends BaseActivity implements View.OnClickListener, BaseAdapterListener<GeneralFile> {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private MenuItem mItemSearch;
    private RecyclerView mRecyclerView;
    private View mContainerLoading;
    private FloatingActionButton mFAB;
    private CoordinatorLayout coordinatorLayout;

    private FilePickerOptions options;
    private int[] colorScheme;

    private ProgressBar mProgressBar;
    private Snackbar mSnackbar;

    private PopupWindow mPopUpWindow;

    private String TAG = getClass().getSimpleName();

    private FileDataManager<GeneralFile> mFileDataManager;

    private Sort mSort;

    private final String ADAPTER_TAG = "tag1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_file_picker_simple);
    }

    private void initExtras() {
        this.mFileDataManager = new FileDataManager<GeneralFile>();
        this.mSort = new Sort();
        options = getIntent().getParcelableExtra(Constant.EXTRA_OPTIONS);
        this.colorScheme = options.getColorScheme();
        Log.d(TAG,"Is enabled : " +options.isEnableDeepScan());
        this.mFileDataManager.addAdapter(new SimpleFileAdapter(new ArrayList<GeneralFile>(0),this,colorScheme,
                !options.isSinglePick()));
    }

    @Override
    public void initView() {

        initExtras();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(options.getHint());

        mContainerLoading = findViewById(R.id.container_loadingView);
        coordinatorLayout = findViewById(R.id.coordinator);
        mFAB = findViewById(R.id.fab_done);
        mFAB.setOnClickListener(this);

        mSearchView = findViewById(R.id.searchview);
        mSearchView.setSubmitButtonEnabled(false);

        mProgressBar = findViewById(R.id.progressBar);

        ImageView closeButton = mSearchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_white);

        EditText txtSearch = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(getResources().getColor(R.color.colorWhite));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mFileDataManager.filterFile(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFileDataManager.filterFile(newText);
                return true;
            }
        });

        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(txtSearch,R.drawable.custom_cursor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mSnackbar = Snackbar.make(coordinatorLayout,getString(R.string.text_loading),Snackbar.LENGTH_INDEFINITE);

        mRecyclerView = findViewById(R.id.recyclerview_file);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFileDataManager.getAdapter());
        registerForContextMenu(mRecyclerView);

        initPermission();
    }

    private void setupColor(Menu menu) {
        if(colorScheme.length > 0)
            mToolbar.setBackgroundColor(colorScheme[0]);
        if(colorScheme.length > 1)
            mToolbar.setTitleTextColor(colorScheme[1]);
        if(colorScheme.length > 2){
            menu.findItem(R.id.action_search).getIcon().setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            menu.findItem(R.id.action_sort).getIcon().setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            Drawable backArrow = ContextCompat.getDrawable(this,R.drawable.ic_arrow_back_white);
            backArrow.setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
            mProgressBar.getIndeterminateDrawable().setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            ((TextView)(findViewById(R.id.text_loading))).setTextColor(colorScheme[2]);
            ((TextView)mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(colorScheme[2]);
        }
        else {
            mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary),
                    PorterDuff.Mode.MULTIPLY);
        }

    }

    private void initPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Constant.REQ_CODE_PERMISSION);
            else getFiles();
        }
        else getFiles();
    }

    private void switchLoadingState(boolean isLoading){
        if(isLoading) {
            if (options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                mContainerLoading.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else if (options.getUpdateMethod() == FileUpdateMethod.STREAM) {
                mContainerLoading.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSnackbar.show();
            }
        }
        else {
            if(options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                mContainerLoading.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            else if(options.getUpdateMethod() == FileUpdateMethod.STREAM){
                mSnackbar.dismiss();
            }
        }
    }

    private void getFiles() {
        switchLoadingState(true);
        FileLoaderHelper.getAllfiles(this, options, new ScanResultCallback() {
            @Override
            public void onFileScanFinished(List<GeneralFile> fileList) {
                if(options.getUpdateMethod() == FileUpdateMethod.BUFFER) {
                    mFileDataManager.replaceData(fileList);
                    mFileDataManager.notifyDataChange();
                }
                mFileDataManager.setHasfinishedLoading(true);
                switchLoadingState(false);
            }

            @Override
            public void onFileScanUpdate(GeneralFile file) {
                mFileDataManager.addData(file);
                // Filter file when search view is active
                // If the commencing file fulfill the query pattern, then add it to adapter
                if(mSearchView.getVisibility() == View.VISIBLE)
                    mFileDataManager.filterFile(mSearchView.getQuery().toString());
                else mFileDataManager.notifyDataInsert();
//                switchLoadingState(false);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constant.REQ_CODE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getFiles();
                else Toast.makeText(this,getString(R.string.text_permission_not_granted),Toast.LENGTH_LONG).show();
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        mItemSearch = menu.findItem(R.id.action_search);
        setupColor(menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.recyclerview_file){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_file,menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            mSearchView.setVisibility(View.VISIBLE);
            mItemSearch.setVisible(false);
            mSearchView.setIconified(false);
            return true;
        }
        else if(i == R.id.action_sort){
            showPopUpWindow();
            return true;
        }
        else if(i == android.R.id.home){
            onBackPressed();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = item.getItemId();
        if (i == R.id.action_open) {
            GeneralFile file = ((SimpleFileAdapter)mFileDataManager.getAdapter()).getCurrentItem();

            String mimeType = file.getMimeType();
            if(mimeType == null){
                Toast.makeText(this,"File type not supported",Toast.LENGTH_SHORT).show();
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

    private void showPopUpWindow() {
        LayoutInflater inflater = getLayoutInflater();
        final View popUpView = inflater.inflate(R.layout.layout_custom_popupwindow,null);
        popUpView.findViewById(R.id.container_sortByDate).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortByName).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortBySize).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortByType).setOnClickListener(this);

        mPopUpWindow = new PopupWindow(popUpView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        switch (mSort.getType()){
            case BY_DATE:
                handlePopUpViewState(R.id.container_sortByDate);
                break;
            case BY_NAME:
                handlePopUpViewState(R.id.container_sortByName);
                break;
            case BY_TYPE:
                handlePopUpViewState(R.id.container_sortByType);
                break;
            case BY_SIZE:
                handlePopUpViewState(R.id.container_sortBySize);
                break;
        }

        mPopUpWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,android.R.color.transparent)));
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.showAtLocation(popUpView, Gravity.TOP | Gravity.RIGHT,0,0);
//        mPopUpWindow.showAsDropDown(mToolbar,0,0);
    }

    private void onPopUpClicked(int id){
        Sort.Type typeNow;

        if (id == R.id.container_sortByName) {
            typeNow = Sort.Type.BY_NAME;
        }
        else if(id == R.id.container_sortByDate){
            typeNow = Sort.Type.BY_DATE;
        }
        else if(id == R.id.container_sortByType){
            typeNow = Sort.Type.BY_TYPE;
        }
        else if(id == R.id.container_sortBySize){
            typeNow = Sort.Type.BY_SIZE;
        }
        else return;

        boolean switchOrder = mSort.getType() == typeNow;

        mSort.setType(typeNow, switchOrder);

        mFileDataManager.sortFile(mSort.getType(),mSort.getOrder());

        mPopUpWindow.dismiss();
    }


    @SuppressLint("RestrictedApi")
    private void handlePopUpViewState(int viewId){
        LinearLayout parent = mPopUpWindow.getContentView().findViewById(viewId);
        for(int i=0;i<parent.getChildCount();i++){
            View view = parent.getChildAt(i);
            if(view instanceof AppCompatRadioButton){
                view.setVisibility(View.VISIBLE);
                if(colorScheme.length > 0)
                    ((AppCompatRadioButton)view).setSupportButtonTintList(ColorStateList.valueOf(colorScheme[0]));
                else ((AppCompatRadioButton)view).setSupportButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.colorPrimary)));
            }
            if(view instanceof ImageView){
                view.setVisibility(View.VISIBLE);
                if(colorScheme.length > 0)
                    ((ImageView)view).setColorFilter(colorScheme[0], PorterDuff.Mode.MULTIPLY);
                Log.d(TAG,"scaleY : "+view.getScaleY());
                if(mSort.getOrder() == Sort.Order.ASCENDING)
                    view.setScaleY(1f);
                else view.setScaleY(-1f);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mSearchView.getVisibility() == View.VISIBLE){
            mSearchView.setVisibility(View.GONE);
            mItemSearch.setVisible(true);
            mSearchView.setQuery("",false);
            mFileDataManager.recoverOriginalData();
        }
        else super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        onPopUpClicked(i);
        if( i == R.id.fab_done){
            returnResult();
        }
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
            getSupportActionBar().setTitle("Selected file ("+selectedFileCount+"/"+options.getFileLimit()+")");
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
                getSupportActionBar().setTitle(options.getHint());
            }
            else getSupportActionBar().setTitle("Selected file ("+selectedFileCount+"/"+options.getFileLimit()+")");
        }
        return true;
    }

    @Override
    public boolean isFileSelected(GeneralFile file) {
        return mFileDataManager.isFileSelected(file.getId());
    }

    private void returnResult(){
        ArrayList<String> results = new ArrayList<>();
        for (Map.Entry<String, GeneralFile> selectedFile : mFileDataManager.getSelectedFile().entrySet()){
            String path = selectedFile.getValue().getFilepath();
            results.add(path);
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(MultiFilePicker.RESULT_FILE,results);
        setResult(RESULT_OK,intent);
        finish();
    }
}
