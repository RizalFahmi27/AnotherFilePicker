package com.github.rzilyn.multifilepicker.loader;


import com.github.rzilyn.multifilepicker.model.GeneralFile;

import java.util.List;

/**
 * Created by Rizal Fahmi on 16-Dec-17.
 */

public interface ScanResultCallback {
    void onFileScanFinished(List<GeneralFile> fileList);
    void onFileScanUpdate(GeneralFile file);
}
