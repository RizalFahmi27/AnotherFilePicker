package com.github.rzilyn.multifilepicker.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rzilyn.multifilepicker.R;
import com.github.rzilyn.multifilepicker.adapters.AlbumAdapter;
import com.github.rzilyn.multifilepicker.interfaces.FragmentInteractionListener;
import com.github.rzilyn.multifilepicker.model.Album;
import com.github.rzilyn.multifilepicker.utils.Constant;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePickerFragment extends Fragment{

  private FragmentInteractionListener mFragmentInteractionListener;
  private RecyclerView mRecyclerView;

  private AlbumAdapter mAdapter;
  private List<Album> albumList;

  public ImagePickerFragment() {
    // Required empty public constructor
  }

  public static ImagePickerFragment newInstance() {
    return new ImagePickerFragment();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (getParentFragment() instanceof FragmentInteractionListener) {
      mFragmentInteractionListener = (FragmentInteractionListener) getParentFragment();
    } else {
      throw new IllegalStateException("Fragment must implement FragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mFragmentInteractionListener = null;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mAdapter = (AlbumAdapter) mFragmentInteractionListener.getFileContract().getAdapter(0);
    mAdapter.setOnClickListener(new OnClickListener() {
      @Override
      public void onCameraClicked(View view) {

      }
    });
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.getRecycledViewPool().setMaxRecycledViews(AlbumAdapter.LIST_VIEW_CAMERA,0);
//    mRecyclerView.getRecycledViewPool().setMaxRecycledViews(AlbumAdapter.GRID_VIEW_CAMERA,0);
  }


  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_image_picker, container, false);

    mRecyclerView = view.findViewById(R.id.recyclerview_album);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setNestedScrollingEnabled(false);
    registerForContextMenu(mRecyclerView);

    return view;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode == Constant.REQ_CODE_PERMISSION_CAMERA){
      if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
        mAdapter.restartCamera();
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
      != PackageManager.PERMISSION_GRANTED ){
      ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},Constant.REQ_CODE_PERMISSION_CAMERA);
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mAdapter.restartCamera();
  }

  public interface OnClickListener{
    void onCameraClicked(View view);
  }
}
