package com.rzilyn.github.multifilepicker.listeners;

import com.rzilyn.github.multifilepicker.utils.Sort;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface ActivityInteractionListener {
    public interface ActivityToFragment {
        void filter(String query);
        void sort(Sort.Type type, Sort.Order order);
        void recoverOriginalData();
    }

    public interface FragmentToActivity{
        boolean isSearchVisible();
        String getSearchQuery();
    }
}
