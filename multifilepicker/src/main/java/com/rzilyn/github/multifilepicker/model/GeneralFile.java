package com.rzilyn.github.multifilepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizal Fahmi on 15-Dec-17.
 */

public class GeneralFile extends BaseFile implements Parcelable{
    private String mimeType;

    public GeneralFile(String id, String filename, String fileType, String dateModified, String filepath, long fileSize, String mimeType) {
        super(id, filename, fileType, dateModified, filepath, fileSize);
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mimeType);
    }

    protected GeneralFile(Parcel in) {
        super(in);
        this.mimeType = in.readString();
    }

    public static final Creator<GeneralFile> CREATOR = new Creator<GeneralFile>() {
        @Override
        public GeneralFile createFromParcel(Parcel source) {
            return new GeneralFile(source);
        }

        @Override
        public GeneralFile[] newArray(int size) {
            return new GeneralFile[size];
        }
    };

    @Override
    public int hashCode() {
        return getFilename().length();
    }

    @Override
    public boolean equals(Object obj) {
        return ((GeneralFile) obj).getId().equals(this.getId());
    }
}
