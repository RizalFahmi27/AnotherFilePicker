package com.github.rzilyn.multifilepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.github.rzilyn.multifilepicker.utils.Sort;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rizal Fahmi on 19-Jan-18.
 */

public class BaseFile implements Parcelable {
  private String id;
  private String path;

  public BaseFile() {

  }

  public String getPath() {
    return path;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public BaseFile(String id, String path) {
    this.path = path;
    this.id = id;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.path);
    dest.writeString(this.id);
  }

  protected BaseFile(Parcel in) {
    this.path = in.readString();
    this.id = in.readString();
  }

  public static final Creator<BaseFile> CREATOR = new Creator<BaseFile>() {
    @Override public BaseFile createFromParcel(Parcel source) {
      return new BaseFile(source);
    }

    @Override public BaseFile[] newArray(int size) {
      return new BaseFile[size];
    }
  };

  public static class FileComparator implements Comparator<BaseFile> {

    private Sort.Type type;
    private Sort.Order order;

    public FileComparator(Sort.Type type, Sort.Order order) {
      this.type = type;
      this.order = order;
    }

    @Override public int compare(BaseFile file1, BaseFile file2) {
      if (file1 instanceof RawFile && file2 instanceof RawFile) {
        RawFile o = (RawFile) file1;
        RawFile o2 = (RawFile) file2;
        switch (type) {
          case BY_NAME:
            if (order == Sort.Order.ASCENDING) {
              return o.getFilename().compareToIgnoreCase(o2.getFilename());
            } else {
              return o2.getFilename().compareToIgnoreCase(o.getFilename());
            }
          case BY_TYPE:
            if (order == Sort.Order.ASCENDING) {
              return o.getFileType().compareToIgnoreCase(o2.getFileType());
            } else {
              return o2.getFileType().compareToIgnoreCase(o.getFileType());
            }
          case BY_DATE:
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
              Date date1 = df.parse(o.getDateModified());
              Date date2 = df.parse(o2.getDateModified());
              if (order == Sort.Order.ASCENDING) {
                return date1.compareTo(date2);
              } else {
                return date2.compareTo(date1);
              }
            } catch (ParseException e) {
              e.printStackTrace();
              return 0;
            }
          case BY_SIZE:
            if (o.getFileSize() > o2.getFileSize()) {
              if (order == Sort.Order.ASCENDING) {
                return 1;
              } else {
                return -1;
              }
            } else if (o.getFileSize() < o2.getFileSize()) {
              if (order == Sort.Order.ASCENDING) {
                return -1;
              } else {
                return 1;
              }
            } else {
              return 0;
            }
          default:
            return 0;
        }
      }
      else return 0;
    }
  }
}
