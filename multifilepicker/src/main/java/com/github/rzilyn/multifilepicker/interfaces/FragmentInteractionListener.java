package com.github.rzilyn.multifilepicker.interfaces;

import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.adapters.FileContract;
import com.github.rzilyn.multifilepicker.model.BaseFile;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface FragmentInteractionListener {
    FileContract<BaseFile> getFileContract();
    FilePickerOptions getFileOptions();

}
