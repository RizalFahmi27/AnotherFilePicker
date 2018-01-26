package com.github.rzilyn.multifilepicker.loader;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.model.BaseFile;
import com.github.rzilyn.multifilepicker.model.GenericFiles;
import com.github.rzilyn.multifilepicker.model.RawFile;
import com.github.rzilyn.multifilepicker.utils.FileUpdateMethod;
import com.github.rzilyn.multifilepicker.utils.Util;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public class FileScanTask extends AsyncTask<Void, BaseFile, List<BaseFile>>{

    private final String TAG = getClass().getSimpleName();
    private final WeakReference<Context> context;
    private final ScanResultCallback<BaseFile> mCallback;
    private List<BaseFile> fileList = new ArrayList<>(0);

    private String[] mFolderFilter = {"Android","cache"};
    private List<String> fileFilter;

    private FilePickerOptions options;
    private int fileId = 0;

    public FileScanTask(Context context, FilePickerOptions options, ScanResultCallback<BaseFile> mCallback){
        this.context = new WeakReference<>(context);
        this.mCallback = mCallback;
        this.options = options;
        this.fileFilter = options.getFileFilters();
    }

    private void getFiles(String initialPath){

        Log.d(TAG,"Current path : "+initialPath);

        File dir = new File(initialPath);
        File[] files = dir.listFiles();
        if(files!=null) {
            for (File file : files) {
                Log.d(TAG, "File name : " + file.getName());
                if (file.isDirectory()) {
                    if (!isPassingFolderFilter(file.getName())) {
                        // skip and do nothing
                        Log.d(TAG, "File skipped : " + file.getName());
                    } else getFiles(file.getAbsolutePath());
                } else if (isPassingFileFilter(Util.getFileExtension(file.getName()))) {
                    fileId++;
                    String fileType = Util.getFileExtension(file.getName());
                    String dateFormat = "dd/MM/yyyy";
                    String modifiedDate = Util.parseDate(file.lastModified(), dateFormat);
                    long fileSize = file.length();
                    String fileName = file.getName();
                    String filePath = file.getAbsolutePath();
                    String mimeType = Util.getMimeType(context.get(), Uri.fromFile(file));
                    GenericFiles
                            genericFile = new GenericFiles(String.valueOf(fileId), fileName, fileType, modifiedDate,
                            filePath, fileSize, mimeType);
                    // Send progress update to ui thread if set to stream method
                    if (options.getUpdateMethod() == FileUpdateMethod.STREAM)
                        publishProgress(genericFile);
                    fileList.add(genericFile);
                }
            }
        }
    }

    private boolean isPassingFolderFilter(String path){
        boolean isValid = true;
        for(String s : mFolderFilter){
            isValid = !path.equals(s) && isValid;
        }

        return isValid && !(path.charAt(0) == '.');
    }

    private boolean isPassingFileFilter(String ext){
        if(fileFilter != null && !fileFilter.isEmpty())
            return fileFilter.contains(ext);
        else return true;
    }

    @Override
    protected void onProgressUpdate(BaseFile... values) {
        mCallback.onFileScanUpdate(values[0]);
    }

    @Override
    protected List<BaseFile> doInBackground(Void... voids) {
        Map<String, File> allStorageDir = Util.getStorageDirectories();
        for(Map.Entry<String, File> entry : allStorageDir.entrySet()){
            getFiles(entry.getValue().getAbsolutePath());
        }
        return fileList;
    }

    @Override
    protected void onPostExecute(List<BaseFile> genericFiles) {
        mCallback.onFileScanFinished(genericFiles);
    }
}
