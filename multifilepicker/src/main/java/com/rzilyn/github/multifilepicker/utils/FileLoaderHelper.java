package com.rzilyn.github.multifilepicker.utils;

import android.content.Context;

import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.loader.FileScanTask;
import com.rzilyn.github.multifilepicker.loader.ScanResultCallback;
import com.rzilyn.github.multifilepicker.model.GeneralFile;

import java.util.List;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public class FileLoaderHelper {
    public static void getAllfiles(Context context, FilePickerOptions options, ScanResultCallback callback){
        new FileScanTask(context, options, callback).execute();
    }
}
