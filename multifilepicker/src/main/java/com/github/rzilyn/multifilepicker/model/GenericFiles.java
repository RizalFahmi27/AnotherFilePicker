package com.github.rzilyn.multifilepicker.model;

import android.os.Parcel;

/**
 * Created by Rizal Fahmi on 15-Dec-17.
 */

public class GenericFiles extends RawFile {
    private String mimeType;

    public GenericFiles(String id, String filename, String fileType, String dateModified, String filepath, long fileSize, String mimeType) {
        super(id, filename, fileType, dateModified, filepath, fileSize);
        this.mimeType = mimeType;
    }

    // Dummy instance
    public GenericFiles(){
        super(null,null,null,null,null,0);
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

    protected GenericFiles(Parcel in) {
        super(in);
        this.mimeType = in.readString();
    }

    public static final Creator<GenericFiles> CREATOR = new Creator<GenericFiles>() {
        @Override
        public GenericFiles createFromParcel(Parcel source) {
            return new GenericFiles(source);
        }

        @Override
        public GenericFiles[] newArray(int size) {
            return new GenericFiles[size];
        }
    };

    @Override
    public int hashCode() {
        return getFilename().length();
    }

    @Override
    public boolean equals(Object obj) {
        return ((GenericFiles) obj).getId().equals(this.getId());
    }
}
