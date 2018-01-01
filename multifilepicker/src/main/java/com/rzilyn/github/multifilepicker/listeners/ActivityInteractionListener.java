package com.rzilyn.github.multifilepicker.listeners;

import com.rzilyn.github.multifilepicker.utils.Sort;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface ActivityInteractionListener {
    interface ActivityToFragment {
        void filter(String query);
        void sort(Sort.Type type, Sort.Order order);
        void recoverOriginalData();
    }

    interface FragmentToActivity{
        boolean isSearchVisible();
        String getSearchQuery();
        Sort.Type getSortType();
        Sort.Order getSortOrder();
    }
}
