package com.github.rzilyn.multifilepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizal Fahmi on 18-Jan-18.
 */

public class Album extends BaseFile {

  private String bucketId;
  private String bucketName;
  private long dateTaken;
  private ArrayList<MediaFiles> mediaFiles;

  public Album(String id, String bucketId, String bucketName, long dateTaken, String data, List<MediaFiles> mediaFiles) {
    super(id, data);
    this.bucketId = bucketId;
    this.bucketName = bucketName;
    this.dateTaken = dateTaken;
    this.mediaFiles = (ArrayList<MediaFiles>) mediaFiles;
  }

  public Album(String id, String bucketId, String bucketName, long dateTaken, String data) {
    super(id, data);
    this.bucketId = bucketId;
    this.bucketName = bucketName;
    this.dateTaken = dateTaken;
  }

  public Album(String bucketName){
    this.bucketName = bucketName;
  }

  public ArrayList<MediaFiles> getMediaFiles() {
    return mediaFiles;
  }

  public void addPhoto(MediaFiles photo){
    if(mediaFiles==null)
      mediaFiles = new ArrayList<>();
    mediaFiles.add(photo);
  }


  public void setMediaFiles(List<MediaFiles> mediaFiles) {
    this.mediaFiles = (ArrayList<MediaFiles>) mediaFiles;
  }

  public String getBucketId() {
    return bucketId;
  }

  public void setBucketId(String bucketId) {
    this.bucketId = bucketId;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public long getDateTaken() {
    return dateTaken;
  }

  public void setDateTaken(long dateTaken) {
    this.dateTaken = dateTaken;
  }

  public int getPhotoCount() {
    if(mediaFiles!=null)
      return mediaFiles.size();
    else return 0;
  }


  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest,flags);
    dest.writeString(this.bucketId);
    dest.writeString(this.bucketName);
    dest.writeLong(this.dateTaken);
    dest.writeList(mediaFiles);
  }

  protected Album(Parcel in) {
    super(in);
    this.bucketId = in.readString();
    this.bucketName = in.readString();
    this.dateTaken = in.readLong();
    this.mediaFiles = in.readArrayList(MediaFiles.class.getClassLoader());
  }

  public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
    @Override public Album createFromParcel(Parcel source) {
      return new Album(source);
    }

    @Override public Album[] newArray(int size) {
      return new Album[size];
    }
  };

}
