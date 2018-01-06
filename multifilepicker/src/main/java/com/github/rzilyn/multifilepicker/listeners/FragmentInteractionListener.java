package com.github.rzilyn.multifilepicker.listeners;

import android.support.annotation.NonNull;

import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.adapters.FileContract;
import com.github.rzilyn.multifilepicker.model.GeneralFile;


/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface FragmentInteractionListener {
    FileContract<GeneralFile> getFileContract();
    FilePickerOptions getFileOptions();

}
