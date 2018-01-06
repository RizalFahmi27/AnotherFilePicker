package com.github.rzilyn.multifilepicker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.rzilyn.multifilepicker.utils.Constant;
import com.github.rzilyn.multifilepicker.utils.Orientation;


public abstract class BaseActivity extends AppCompatActivity {

    protected void onCreate(@NonNull Bundle savedInstanceState, @LayoutRes int layout) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        FilePickerOptions options;

        if(getIntent() !=null){
            options = getIntent().getParcelableExtra(Constant.EXTRA_OPTIONS);
        }
        else throw new IllegalArgumentException("Bundle cannot be null");

        Orientation orientation = options.getOrientation();
        if(orientation == Orientation.LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else if(orientation == Orientation.PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();

    }


    public abstract void initView();
}
