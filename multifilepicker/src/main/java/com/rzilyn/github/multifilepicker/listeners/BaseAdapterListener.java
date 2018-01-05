package com.rzilyn.github.multifilepicker.listeners;

/**
 * Created by Rizal Fahmi on 21-Dec-17.
 */

public interface BaseAdapterListener<T> {
    boolean onItemSelected(T file);
    boolean onItemUnselected(T file);
    boolean isFileSelected(T key);
    void onItemLongClicked(T file, int filePosition, int adapterPosition);
    void onItemLongClicked(T file);
}
