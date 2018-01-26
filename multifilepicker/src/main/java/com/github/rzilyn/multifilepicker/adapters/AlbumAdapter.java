package com.github.rzilyn.multifilepicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.rzilyn.multifilepicker.R;
import com.github.rzilyn.multifilepicker.fragments.ImagePickerFragment;
import com.github.rzilyn.multifilepicker.interfaces.BaseAdapterListener;
import com.github.rzilyn.multifilepicker.interfaces.CameraSupport;
import com.github.rzilyn.multifilepicker.model.Album;
import com.github.rzilyn.multifilepicker.utils.Util;
import com.github.rzilyn.multifilepicker.view_component.AutoFitTextureView;
import com.github.rzilyn.multifilepicker.view_component.CameraPreview;

import java.io.File;
import java.util.List;

/**
 * Created by Rizal Fahmi on 21-Jan-18.
 */

public class AlbumAdapter extends BaseFileAdapter<Album> {

    public static final int GRID_VIEW = 1;
    public static final int GRID_VIEW_CAMERA = -1;
    public static final int LIST_VIEW = 2;
    public static final int LIST_VIEW_CAMERA = -2;

    private BaseAdapterListener mBaseListener;
    private ImagePickerFragment.OnClickListener mOnClickListener;

    // Taking attention on camera
    private CameraSupport mCameraSupport;

    private int VIEW_TYPE = LIST_VIEW;

    private boolean enableCamera = false;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == LIST_VIEW) {
            View listView =
                    inflater.inflate(R.layout.layout_image_placeholder_list_album, parent, false);
            return new ListHolder(listView);
        } else if (viewType == LIST_VIEW_CAMERA) {
            View listViewCamera = inflater.inflate(R.layout.layout_camera_placeholder_list_album, parent, false);
            return new ListHolderCamera(listViewCamera);
        } else if (viewType == GRID_VIEW) {
            View gridView =
                    inflater.inflate(R.layout.layout_image_placeholder_grid_album, parent, false);
            return new GridHolder(gridView);
        } else if (viewType == GRID_VIEW_CAMERA) {
            View gridViewCamera = inflater.inflate(R.layout.layout_camera_placeholder_grid_album, parent, false);
            return new GridHolderCamera(gridViewCamera);
        } else {
            return null;
        }
    }


    public AlbumAdapter(List<Album> albumList, Context mContext, int[] colorScheme, BaseAdapterListener listener, boolean enableCamera) {
        super(albumList, mContext, colorScheme);
        this.mBaseListener = listener;
        this.enableCamera = enableCamera;
    }

    public void setViewType(int viewType) {
        this.VIEW_TYPE = viewType;
    }

    public void enableCamera(boolean enableCamera) {
        this.enableCamera = enableCamera;
        if (enableCamera) {
            fileList.add(0, new Album(mContext.getString(R.string.text_camera)));
        }
    }

    public void setOnClickListener(ImagePickerFragment.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Album album = fileList.get(position);
        switch (getItemViewType(position)) {
            case LIST_VIEW:
                ListHolder listHolder = (ListHolder) holder;
                Glide.with(mContext).load(new File(album.getPath()))
                        .apply(RequestOptions.centerCropTransform())
                        .into(listHolder.imagePhoto);
                listHolder.textBucketDate.setText(Util.parseDate(album.getDateTaken(), "dd MMMM yyyy"));
                listHolder.textBucketName.setText(album.getBucketName());
                listHolder.textPhotoCount.setText(String.valueOf(album.getPhotoCount()));
                break;

            case LIST_VIEW_CAMERA:
                final ListHolderCamera listHolderCamera = (ListHolderCamera) holder;
                mCameraSupport = Util.getCamera(mContext,
                        listHolderCamera.cameraView, true);
                CameraPreview cameraPreview = new CameraPreview(mCameraSupport);
                listHolderCamera.cameraView.setSurfaceTextureListener(cameraPreview);
                listHolderCamera.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnClickListener.onCameraClicked(listHolderCamera.cameraView);
                    }
                });
                break;

            case GRID_VIEW:
                break;

        }
    }

    public void restartCamera() {
        if (mCameraSupport != null) {
            mCameraSupport.destroy();
            notifyItemChanged(0);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ListHolderCamera) {
            ListHolderCamera lc = (ListHolderCamera) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (enableCamera) {
            if (position == 0)
                return (VIEW_TYPE * -1);
            else return VIEW_TYPE;
        } else return VIEW_TYPE;
    }

    static class GridHolder extends RecyclerView.ViewHolder {

        public GridHolder(View itemView) {
            super(itemView);
        }
    }

    static class GridHolderCamera extends RecyclerView.ViewHolder {

        public GridHolderCamera(View itemView) {
            super(itemView);
        }
    }

    static class ListHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;
        TextView textBucketName;
        TextView textBucketDate;
        TextView textPhotoCount;
        ImageView imageBucketIndicator;

        ListHolder(View itemView) {
            super(itemView);
            imagePhoto = itemView.findViewById(R.id.image_photo);
            textBucketName = itemView.findViewById(R.id.text_bucketName);
            textBucketDate = itemView.findViewById(R.id.text_bucketDate);
            textPhotoCount = itemView.findViewById(R.id.text_bucketPhotoCount);
            imageBucketIndicator = itemView.findViewById(R.id.image_folderIndicator);
        }
    }

    static class ListHolderCamera extends RecyclerView.ViewHolder {

        ImageView imagePhoto;
        AutoFitTextureView cameraView;

        ListHolderCamera(View itemView) {
            super(itemView);
            imagePhoto = itemView.findViewById(R.id.image_photo);
            cameraView = itemView.findViewById(R.id.surfaceView);
        }
    }
}
