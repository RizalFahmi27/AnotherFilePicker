package com.github.rzilyn.multifilepicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.github.rzilyn.multifilepicker.fragments.FilePickerFragment;
import com.github.rzilyn.multifilepicker.fragments.ImagePickerFragment;
import com.github.rzilyn.multifilepicker.interfaces.ActivityInteractionListener;
import com.github.rzilyn.multifilepicker.utils.ActivityUtil;
import com.github.rzilyn.multifilepicker.utils.Constant;
import com.github.rzilyn.multifilepicker.utils.PickType;
import com.github.rzilyn.multifilepicker.utils.Sort;

import java.lang.reflect.Field;


public class FilePickerActivity extends BaseActivity implements View.OnClickListener,
        ActivityInteractionListener.FragmentToActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private MenuItem mItemSearch;
    private int tabCurrentPosition = 0;

    private FilePickerOptions options;
    private int[] colorScheme;

    private PopupWindow mPopUpWindow;

    private ActivityInteractionListener.ActivityToFragment mActivityListener;

    private String TAG = getClass().getSimpleName();

    private Sort mSort;

    private boolean swapFragment = false;

    private PickType PICK_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_file_picker_simple);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (swapFragment) {
            showFragment();
        }
    }

    private void initExtras() {
        mSort = new Sort();
        options = getIntent().getParcelableExtra(Constant.EXTRA_OPTIONS);
        colorScheme = options.getColorScheme();
        PICK_TYPE = (PickType) getIntent().getSerializableExtra(Constant.EXTRA_PICK_TYPE);
    }

    @Override
    public void initView() {

        initExtras();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(options.getHint());

        mSearchView = findViewById(R.id.searchview);
        mSearchView.setSubmitButtonEnabled(false);

        ImageView closeButton = mSearchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_white);

        final EditText txtSearch = mSearchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(getResources().getColor(R.color.colorWhite));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActivityListener.filter(query, tabCurrentPosition);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mActivityListener.filter(newText, tabCurrentPosition);
                return true;
            }
        });

        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(txtSearch, R.drawable.custom_cursor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        initPermission();
    }

    private void setupColor(Menu menu) {
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_black);
        backArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), Mode.SRC_ATOP);
        if (colorScheme.length > 0) {
            mToolbar.setBackgroundColor(colorScheme[0]);
        }
        if (colorScheme.length > 1) {
            mToolbar.setTitleTextColor(colorScheme[1]);
        }
        if (colorScheme.length > 2) {
            menu.findItem(R.id.action_search).getIcon()
                    .setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            menu.findItem(R.id.action_sort).getIcon()
                    .setColorFilter(colorScheme[2], PorterDuff.Mode.MULTIPLY);
            backArrow.setColorFilter(colorScheme[2], Mode.SRC_ATOP);
            ((TextView) (findViewById(R.id.text_loading))).setTextColor(colorScheme[2]);
        }
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat
                        .requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constant.REQ_CODE_PERMISSION_READ_STORAGE);
            } else {
                showFragment();
            }
        } else {
            showFragment();
        }
        swapFragment = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.REQ_CODE_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    swapFragment = true;
                } else {
                    Toast.makeText(this, getString(R.string.text_permission_not_granted), Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                return;
            case Constant.REQ_CODE_PERMISSION_CAMERA:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(FilePickerFragment.class.getSimpleName())
                        .getChildFragmentManager().findFragmentByTag(ImagePickerFragment.class.getSimpleName());
                if(fragment!=null){
                    fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
                }
                return;
        }
    }

    private void showFragment() {
        FilePickerFragment fragment = FilePickerFragment.newInstance(options, PICK_TYPE);
        ActivityUtil.addFragment(getSupportFragmentManager(), fragment, R.id.container_fragment);
        this.mActivityListener = fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        mItemSearch = menu.findItem(R.id.action_search);
        setupColor(menu);
        if (PICK_TYPE == PickType.PHOTO) {
            mSearchView.setVisibility(View.GONE);
            mItemSearch.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            mSearchView.setVisibility(View.VISIBLE);
            mItemSearch.setVisible(false);
            mSearchView.setIconified(false);
            return true;
        } else if (i == R.id.action_sort) {
            showPopUpWindow();
            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void showPopUpWindow() {
        LayoutInflater inflater = getLayoutInflater();
        final View popUpView = inflater.inflate(R.layout.layout_custom_popupwindow, null);
        popUpView.findViewById(R.id.container_sortByDate).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortByName).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortBySize).setOnClickListener(this);
        popUpView.findViewById(R.id.container_sortByType).setOnClickListener(this);

        if (options.isTabEnabled() || PICK_TYPE == PickType.PHOTO) {
            popUpView.findViewById(R.id.container_sortByType).setVisibility(View.GONE);
        }

        mPopUpWindow = new PopupWindow(popUpView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        switch (mSort.getType()) {
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

        mPopUpWindow.setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.showAtLocation(popUpView, Gravity.TOP | Gravity.RIGHT, 0, 0);
//        mPopUpWindow.showAsDropDown(mToolbar,0,0);
    }

    private void onPopUpClicked(int id) {
        Sort.Type typeNow;

        if (id == R.id.container_sortByName) {
            typeNow = Sort.Type.BY_NAME;
        } else if (id == R.id.container_sortByDate) {
            typeNow = Sort.Type.BY_DATE;
        } else if (id == R.id.container_sortByType) {
            typeNow = Sort.Type.BY_TYPE;
        } else if (id == R.id.container_sortBySize) {
            typeNow = Sort.Type.BY_SIZE;
        } else {
            return;
        }

        boolean switchOrder = mSort.getType() == typeNow;

        mSort.setType(typeNow, switchOrder);

        mActivityListener.sort(mSort.getType(), mSort.getOrder(), tabCurrentPosition);

        mPopUpWindow.dismiss();
    }


    @SuppressLint("RestrictedApi")
    private void handlePopUpViewState(int viewId) {
        LinearLayout parent = mPopUpWindow.getContentView().findViewById(viewId);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof AppCompatRadioButton) {
                view.setVisibility(View.VISIBLE);
                if (colorScheme.length > 0) {
                    ((AppCompatRadioButton) view)
                            .setSupportButtonTintList(ColorStateList.valueOf(colorScheme[0]));
                } else {
                    ((AppCompatRadioButton) view).setSupportButtonTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
                }
            }
            if (view instanceof ImageView) {
                view.setVisibility(View.VISIBLE);
                if (colorScheme.length > 0) {
                    ((ImageView) view).setColorFilter(colorScheme[0], PorterDuff.Mode.MULTIPLY);
                }
                Log.d(TAG, "scaleY : " + view.getScaleY());
                if (mSort.getOrder() == Sort.Order.ASCENDING) {
                    view.setScaleY(1f);
                } else {
                    view.setScaleY(-1f);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.getVisibility() == View.VISIBLE) {
            mSearchView.setVisibility(View.GONE);
            mItemSearch.setVisible(true);
            mSearchView.setQuery("", false);
            mActivityListener.recoverOriginalData(tabCurrentPosition);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        onPopUpClicked(i);
    }

    @Override
    public boolean isSearchVisible() {
        return mSearchView.getVisibility() == View.VISIBLE;
    }

    @Override
    public String getSearchQuery() {
        return mSearchView.getQuery().toString();
    }

    @Override
    public Sort.Type getSortType() {
        return mSort.getType();
    }

    @Override
    public Sort.Order getSortOrder() {
        return mSort.getOrder();
    }

    @Override
    public void setActiveTab(int position) {
        this.tabCurrentPosition = position;
    }
}
