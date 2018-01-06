package com.github.rzilyn.multifilepicker.utils;

import android.content.Context;


import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.loader.FileScanTask;
import com.github.rzilyn.multifilepicker.loader.ScanResultCallback;

import java.util.List;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public class FileLoaderHelper {
    public static void getAllfiles(Context context, FilePickerOptions options, ScanResultCallback callback){
        new FileScanTask(context, options, callback).execute();
    }
}
