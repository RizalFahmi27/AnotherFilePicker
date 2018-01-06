package com.github.rzilyn.multifilepicker;

import android.app.Activity;
import android.content.Intent;

import com.github.rzilyn.multifilepicker.utils.Constant;


/**
 * Created by Rizal Fahmi on 14-Dec-17.
 */

public class MultiFilePicker {

    public static final String RESULT_FILE = "result_file";

    public static void pickFile(Activity activity, FilePickerOptions options, int requestCode){
        Intent intent = new Intent(activity,FilePickerActivity.class);
        intent.putExtra(Constant.EXTRA_OPTIONS,options);
        activity.startActivityForResult(intent,requestCode);
    }
}
