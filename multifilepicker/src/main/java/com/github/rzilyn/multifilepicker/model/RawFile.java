package com.github.rzilyn.multifilepicker.model;

import android.os.Parcel;
import com.github.rzilyn.multifilepicker.utils.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rizal Fahmi on 15-Dec-17.
 */

public class RawFile extends BaseFile {
  private String filename;
  private String fileType;
  private String dateModified;
  private long fileSize;

  public RawFile(String id, String filename, String fileType, String dateModified, String filepath,
      long fileSize) {
    super(id, filepath);
    this.filename = filename;
    this.fileType = fileType;
    this.dateModified = dateModified;
    this.fileSize = fileSize;
  }

  public RawFile() {

  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getDateModified() {
    return dateModified;
  }

  public void setDateModified(String dateModified) {
    this.dateModified = dateModified;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.filename);
    dest.writeString(this.fileType);
    dest.writeString(this.dateModified);
    dest.writeLong(this.fileSize);
  }

  protected RawFile(Parcel in) {
    super(in);
    this.filename = in.readString();
    this.fileType = in.readString();
    this.dateModified = in.readString();
    this.fileSize = in.readLong();
  }

  public static final Creator<RawFile> CREATOR = new Creator<RawFile>() {
    @Override public RawFile createFromParcel(Parcel source) {
      return new RawFile(source);
    }

    @Override public RawFile[] newArray(int size) {
      return new RawFile[size];
    }
  };
}
