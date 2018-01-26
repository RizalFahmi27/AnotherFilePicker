package com.github.rzilyn.multifilepicker.model;

import android.database.Cursor;
import android.media.ExifInterface;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import com.github.rzilyn.multifilepicker.utils.Sort;
import com.github.rzilyn.multifilepicker.utils.Util;
import java.io.IOException;

/**
 * Created by Rizal Fahmi on 18-Jan-18.
 */

public class MediaFiles extends RawFile {
  private long width;
  private long height;
  private long dateTaken;
  private long dateAdded;
  private String device;
  private String deviceModel;
  private String orientation;
  private String latitude;
  private String longitude;
  private String focalLength;
  private String whiteBalance;
  private String flash;
  private String description;


  public MediaFiles(@NonNull Cursor cursor, boolean advanced){
    this.dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
    String dateModified = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
    String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
    String fileType = Util.getFileExtension(path);
    String filename = Util.getFilename(path);
    long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

    setId(id);
    setFileSize(fileSize);
    setFilename(filename);
    setDateModified(dateModified);
    setPath(path);
    setFileType(fileType);

    if(advanced){
      this.dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));
      this.width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
      this.height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
      this.latitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
      this.longitude = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
      this.description = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION));
      this.orientation = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION));
      try {
        ExifInterface exif = new ExifInterface(path);
        this.device = exif.getAttribute(ExifInterface.TAG_MAKE);
        this.deviceModel = exif.getAttribute(ExifInterface.TAG_MODEL);
        this.flash = exif.getAttribute(ExifInterface.TAG_FLASH);
        this.whiteBalance = exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
        this.focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public long getWidth() {
    return width;
  }

  public void setWidth(long width) {
    this.width = width;
  }

  public long getHeight() {
    return height;
  }

  public void setHeight(long height) {
    this.height = height;
  }

  public long getDateTaken() {
    return dateTaken;
  }

  public void setDateTaken(long dateTaken) {
    this.dateTaken = dateTaken;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getOrientation() {
    return orientation;
  }

  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public long getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(long dateAdded) {
    this.dateAdded = dateAdded;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public MediaFiles(String id, String filename, String fileType, String dateModified,
      String filepath, long fileSize, long width, long height, long dateTaken, long dateAdded,
      String device, String deviceModel, String orientation, String latitude, String longitude,
      String focalLength, String whiteBalance, String flash, String description) {
    super(id, filename, fileType, dateModified, filepath, fileSize);
    this.width = width;
    this.height = height;
    this.dateTaken = dateTaken;
    this.dateAdded = dateAdded;
    this.device = device;
    this.deviceModel = deviceModel;
    this.orientation = orientation;
    this.latitude = latitude;
    this.longitude = longitude;
    this.focalLength = focalLength;
    this.whiteBalance = whiteBalance;
    this.flash = flash;
    this.description = description;
  }

  public String getFocalLength() {

    return focalLength;
  }

  public void setFocalLength(String focalLength) {
    this.focalLength = focalLength;
  }

  public String getWhiteBalance() {
    return whiteBalance;
  }

  public void setWhiteBalance(String whiteBalance) {
    this.whiteBalance = whiteBalance;
  }

  public String getFlash() {
    return flash;
  }

  public void setFlash(String flash) {
    this.flash = flash;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeLong(this.width);
    dest.writeLong(this.height);
    dest.writeLong(this.dateTaken);
    dest.writeLong(this.dateAdded);
    dest.writeString(this.device);
    dest.writeString(this.deviceModel);
    dest.writeString(this.orientation);
    dest.writeString(this.latitude);
    dest.writeString(this.longitude);
    dest.writeString(this.focalLength);
    dest.writeString(this.whiteBalance);
    dest.writeString(this.flash);
    dest.writeString(this.description);
  }

  protected MediaFiles(Parcel in) {
    super(in);
    this.width = in.readLong();
    this.height = in.readLong();
    this.dateTaken = in.readLong();
    this.dateAdded = in.readLong();
    this.device = in.readString();
    this.deviceModel = in.readString();
    this.orientation = in.readString();
    this.latitude = in.readString();
    this.longitude = in.readString();
    this.focalLength = in.readString();
    this.whiteBalance = in.readString();
    this.flash = in.readString();
    this.description = in.readString();
  }

  public static final Creator<MediaFiles> CREATOR = new Creator<MediaFiles>() {
    @Override public MediaFiles createFromParcel(Parcel source) {
      return new MediaFiles(source);
    }

    @Override public MediaFiles[] newArray(int size) {
      return new MediaFiles[size];
    }
  };

}
