package com.github.rzilyn.multifilepicker.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.github.rzilyn.multifilepicker.utils.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rizal Fahmi on 15-Dec-17.
 */

public class BaseFile implements Parcelable {
    private String id;
    private String filename;
    private String fileType;
    private String dateModified;
    private String filepath;
    private long fileSize;

    public BaseFile(String id, String filename, String fileType, String dateModified, String filepath, long fileSize) {
        this.filename = filename;
        this.id = id;
        this.fileType = fileType;
        this.dateModified = dateModified;
        this.filepath = filepath;
        this.fileSize = fileSize;
    }

    public static final Creator<BaseFile> CREATOR = new Creator<BaseFile>() {
        @Override
        public BaseFile createFromParcel(Parcel in) {
            return new BaseFile(in);
        }

        @Override
        public BaseFile[] newArray(int size) {
            return new BaseFile[size];
        }
    };

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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filename);
        dest.writeString(this.fileType);
        dest.writeString(this.dateModified);
        dest.writeString(this.filepath);
        dest.writeLong(this.fileSize);
        dest.writeString(this.id);
    }

    public BaseFile() {

    }

    protected BaseFile(Parcel in) {
        this.filename = in.readString();
        this.fileType = in.readString();
        this.dateModified = in.readString();
        this.filepath = in.readString();
        this.fileSize = in.readLong();
        this.id = in.readString();
    }

    public static class FileComparator implements Comparator<BaseFile>{

        private Sort.Type type;
        private Sort.Order order;

        public FileComparator(Sort.Type type, Sort.Order order){
            this.type = type;
            this.order = order;
        }

        @Override
        public int compare(BaseFile o, BaseFile o2) {
            switch (type){
                case BY_NAME:
                    if(order == Sort.Order.ASCENDING)
                        return o.getFilename().compareToIgnoreCase(o2.getFilename());
                    else return o2.getFilename().compareToIgnoreCase(o.getFilename());
                case BY_TYPE:
                    if(order == Sort.Order.ASCENDING)
                        return o.getFileType().compareToIgnoreCase(o2.getFileType());
                    else return o2.getFileType().compareToIgnoreCase(o.getFileType());
                case BY_DATE:
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date1 = df.parse(o.getDateModified());
                        Date date2 = df.parse(o2.getDateModified());
                        if(order == Sort.Order.ASCENDING)
                            return date1.compareTo(date2);
                        else return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                case BY_SIZE:
                        if(o.getFileSize() > o2.getFileSize()){
                            if(order == Sort.Order.ASCENDING)
                                return 1;
                            else return -1;
                        }
                        else if(o.getFileSize() < o2.getFileSize()){
                            if(order == Sort.Order.ASCENDING)
                                return -1;
                            else return 1;
                        }
                        else return 0;
                default:
                    return 0;
            }
        }
    }

}
