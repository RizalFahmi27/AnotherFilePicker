package com.github.rzilyn.multifilepicker;

import android.app.Activity;
import android.content.Intent;

import com.github.rzilyn.multifilepicker.utils.Constant;
import com.github.rzilyn.multifilepicker.utils.PickType;

/**
 * Created by Rizal Fahmi on 14-Dec-17.
 */

public class MultiFilePicker {

    public static final String RESULT_FILE = "result_file";

    private final String PICK_TYPE_FILE = "pick_file";
    private final String PICK_TYPE_PHOTO = "pick_photo";

    public static void pickFile(Activity activity, FilePickerOptions options, int requestCode){
        Intent intent = new Intent(activity,FilePickerActivity.class);
        intent.putExtra(Constant.EXTRA_OPTIONS,options);
        intent.putExtra(Constant.EXTRA_PICK_TYPE, PickType.FILE);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void pickPhoto(Activity activity, FilePickerOptions options, int requestCode){
      Intent intent = new Intent(activity,FilePickerActivity.class);
      intent.putExtra(Constant.EXTRA_OPTIONS,options);
      intent.putExtra(Constant.EXTRA_PICK_TYPE,PickType.PHOTO);
      activity.startActivityForResult(intent,requestCode);
    }
}
