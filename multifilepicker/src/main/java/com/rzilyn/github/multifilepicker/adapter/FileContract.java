package com.rzilyn.github.multifilepicker.adapter;

import android.widget.BaseAdapter;

import com.rzilyn.github.multifilepicker.model.BaseFile;
import com.rzilyn.github.multifilepicker.model.GeneralFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public interface FileContract<T> {
    void addAdapter(BaseFileAdapter adapter);
    void replaceData(List<T> fileList);
    void replaceTempData(List<T> fileList);
    void addData(T file);
    void addTempData(T file);
    void addSelectedFile(String key, T file);
    void removeSelectedFile(String key);
    Map<String, T> getSelectedFile();
    List<T> getData();
}
