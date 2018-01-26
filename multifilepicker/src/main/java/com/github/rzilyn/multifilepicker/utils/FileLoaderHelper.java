package com.github.rzilyn.multifilepicker.utils;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.loader.CursorResultCallback;
import com.github.rzilyn.multifilepicker.loader.FileScanTask;
import com.github.rzilyn.multifilepicker.loader.MediaLoaderCallback;
import com.github.rzilyn.multifilepicker.loader.ScanResultCallback;

import com.github.rzilyn.multifilepicker.model.BaseFile;
import com.github.rzilyn.multifilepicker.model.GenericFiles;
import com.github.rzilyn.multifilepicker.model.RawFile;
import java.util.List;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public class FileLoaderHelper {

  public static void getAllfiles(Context context, FilePickerOptions options,
      ScanResultCallback<BaseFile> callback) {
    new FileScanTask(context, options, callback).execute();
  }

  public static void getPhotoList(FragmentActivity activity, FilePickerOptions options, @NonNull Bundle args, CursorResultCallback callback){
    LoaderManager loaderManager = activity.getSupportLoaderManager();
    if(loaderManager.getLoader(Constant.LOADER_IMAGE)==null){
      loaderManager.initLoader(Constant.LOADER_IMAGE,args,new MediaLoaderCallback(activity,callback,options));
    }
    else loaderManager.restartLoader(Constant.LOADER_IMAGE,args,new MediaLoaderCallback(activity,callback,options));
  }
}
