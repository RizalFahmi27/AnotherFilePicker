package com.rzilyn.github.multifilepicker.listeners;

import com.rzilyn.github.multifilepicker.utils.Sort;

/**
 * Created by Rizal Fahmi on 27-Dec-17.
 */

public interface ActivityInteractionListener {
    interface ActivityToFragment {
        void filter(String query, int position);
        void sort(Sort.Type type, Sort.Order order, int position);
        void recoverOriginalData(int position);
    }

    interface FragmentToActivity{
        boolean isSearchVisible();
        String getSearchQuery();
        Sort.Type getSortType();
        Sort.Order getSortOrder();
        void setActiveTab(int position);
    }
}
