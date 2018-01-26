package com.github.rzilyn.multifilepicker.loader;

import com.github.rzilyn.multifilepicker.model.Album;
import com.github.rzilyn.multifilepicker.model.BaseFile;

/**
 * Created by Rizal Fahmi on 19-Jan-18.
 */

public interface CursorResultCallback extends ScanResultCallback<BaseFile> {
  void onFileScanEmpty();
}
