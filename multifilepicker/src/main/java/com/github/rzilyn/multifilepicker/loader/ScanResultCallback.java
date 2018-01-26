package com.github.rzilyn.multifilepicker.loader;


import com.github.rzilyn.multifilepicker.model.BaseFile;

import java.util.List;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public interface ScanResultCallback<T extends BaseFile> {
    void onFileScanFinished(List<T> fileList);
    void onFileScanUpdate(T file);
}
