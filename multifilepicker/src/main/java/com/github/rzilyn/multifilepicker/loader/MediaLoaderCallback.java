package com.github.rzilyn.multifilepicker.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import com.github.rzilyn.multifilepicker.FilePickerOptions;
import com.github.rzilyn.multifilepicker.model.Album;
import com.github.rzilyn.multifilepicker.model.BaseFile;
import com.github.rzilyn.multifilepicker.model.MediaFiles;
import com.github.rzilyn.multifilepicker.utils.Constant;
import com.github.rzilyn.multifilepicker.utils.PickType;
import com.github.rzilyn.multifilepicker.utils.Sort;
import com.github.rzilyn.multifilepicker.utils.Util;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizal Fahmi on 18-Jan-18.
 */

public class MediaLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

  private WeakReference<Context> mContext;
  private CursorResultCallback mCallback;
  private FilePickerOptions mOptions;

  public MediaLoaderCallback(Context context, CursorResultCallback mCallback, FilePickerOptions options) {
    this.mContext = new WeakReference<>(context);
    this.mCallback = mCallback;
    this.mOptions = options;
  }

  @Override
  public Loader onCreateLoader(int id, Bundle args) {
    return new CursorMediaLoader(mContext.get(),args);
  }

  @Override public void onLoadFinished(Loader loader, Cursor cursor) {
    if(cursor == null) {
      mCallback.onFileScanEmpty();
      return;
    }

    List<BaseFile> albumList = new ArrayList<>();
    List<String> bucketIds = new ArrayList<>();
    List<MediaFiles> mediaFiles = new ArrayList<>();

    cursor.moveToFirst();

    do {
      String photoId = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID));
      String bucketId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
      String bucketName = cursor.getString(cursor.getColumnIndexOrThrow(
          MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
      String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
      long dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));

      Album album = new Album(photoId,bucketId,bucketName,dateTaken,path);
      MediaFiles photo = new MediaFiles(cursor,mOptions.isPreviewEnabled());

      if(!bucketIds.contains(bucketId)){
        if(mOptions.getEnableGifSupport()){
          if(Util.isExtensionOf(path,Util.FILE_GIF)){
            album.addPhoto(photo);
          }
        }
        else {
          album.addPhoto(photo);
        }
        bucketIds.add(bucketId);
        albumList.add(album);
      }
      else {
        ((Album)albumList.get(bucketIds.indexOf(bucketId))).addPhoto(photo);
      }

      mediaFiles.add(photo);

      //((Album)albumList.get(0)).addPhoto(photo);
      //((Album)albumList.get(0)).setBucketId(photo.getId());
      //((Album)albumList.get(0)).setDateTaken(photo.getDateTaken());
      //albumList.get(0).setPath(photo.getPath());

      mCallback.onFileScanUpdate(album);

    }
    while (cursor.moveToNext());
    cursor.close();

    // Get first inserted photo and set the "All photos" album

    Album allPhotosAlbum = new Album("All photos");
    allPhotosAlbum.setMediaFiles(mediaFiles);

    MediaFiles lastPhoto = mediaFiles.get(0);

    allPhotosAlbum.setBucketId(lastPhoto.getId());
    allPhotosAlbum.setDateTaken(lastPhoto.getDateTaken());
    allPhotosAlbum.setPath(lastPhoto.getPath());

    if(mOptions.isCameraEnabled()){
      albumList.add(0,new Album("Camera"));
      albumList.add(1,allPhotosAlbum);
    }
    else albumList.add(0,allPhotosAlbum);

    mCallback.onFileScanFinished(albumList);

  }

  @Override public void onLoaderReset(Loader loader) {

  }

  private static class CursorMediaLoader extends CursorLoader {

    private String[] projectionImage = {
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.ORIENTATION,
        MediaStore.Images.Media.DESCRIPTION
    };

    private String[] projectionVideo ={
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DATE_MODIFIED,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.ALBUM,
        MediaStore.Video.Media.ARTIST,
        MediaStore.Video.Media.BOOKMARK,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.RESOLUTION,
        MediaStore.Video.Media.LATITUDE,
        MediaStore.Video.Media.LONGITUDE,
        MediaStore.Video.Media.DESCRIPTION
    };

    private final Uri uri = MediaStore.Files.getContentUri("external");
    private final String sortOrder = MediaStore.Images.Media._ID + " DESC";

    public CursorMediaLoader(@NonNull Context context, Bundle args) {
      super(context);

      PickType mediaType = (PickType) args.getSerializable(Constant.EXTRA_PICK_TYPE);
      String bucketId = args.getString(Constant.EXTRA_BUCKET_ID,null);

      String selection = "";
      if(mediaType == PickType.PHOTO)
        selection += MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
      else selection += MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

      setUri(uri);
      setSortOrder(sortOrder);

      if(bucketId!=null){
        selection+= " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'";
      }
      setSelection(selection);
      // Will not use projection because we will get all available information
      setProjection(null);
    }

  }
}
