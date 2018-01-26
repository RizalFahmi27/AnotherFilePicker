package com.github.rzilyn.multifilepicker.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Rizal Fahmi on 24-Dec-17.
 */

public class ActivityUtil {

  public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
      int containerId, @NonNull String tag) {
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.add(containerId, fragment, tag);
    ft.commit();
  }

  public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int containerId, @Nullable String tag,
                                 boolean addToBackStack){
    FragmentTransaction ft = fragmentManager.beginTransaction();
    if(tag == null || tag.equals(""))
      tag = fragment.getClass().getSimpleName();
    ft.add(containerId,fragment,tag);
    if(addToBackStack)
      ft.addToBackStack(tag);
    ft.commit();
  }

  public static void addFragment(FragmentManager fragmentManager, Fragment fragment,
      int containerId) {
    String tag = fragment.getClass().getSimpleName();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.add(containerId, fragment, tag);
    ft.commit();
  }

}
