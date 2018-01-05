package com.rzilyn.github.multifilepicker.listeners;

import android.support.annotation.NonNull;

import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.adapters.FileContract;
import com.rzilyn.github.multifilepicker.model.GeneralFile;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface FragmentInteractionListener {
    FileContract<GeneralFile> getFileContract();
    FilePickerOptions getFileOptions();

}
