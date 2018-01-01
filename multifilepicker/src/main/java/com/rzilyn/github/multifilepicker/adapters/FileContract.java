package com.rzilyn.github.multifilepicker.adapters;

import com.rzilyn.github.multifilepicker.adapters.BaseFileAdapter;
import com.rzilyn.github.multifilepicker.utils.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public interface FileContract<T> {
    List<T> getData();
    BaseFileAdapter getAdapter(int position);
//    void setData(String projection, Sort.Type sortType, Sort.Order sortOrder, String searchQuery, int position);
}
