package com.rzilyn.github.multifilepicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.rzilyn.github.multifilepicker.utils.FileTypePreset;
import com.rzilyn.github.multifilepicker.utils.FileUpdateMethod;
import com.rzilyn.github.multifilepicker.utils.Orientation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rizal Fahmi on 13-Dec-17.
 */

public class FilePickerOptions implements Parcelable {

    private Orientation orientation;

    private int singlePick;

    private int fileLimit = 1;

    private FileUpdateMethod updateMethod;

    private int enableDeepScan;

    private int[] colorScheme;

    private String hint;

    private List<String> fileFilters = new ArrayList<>();

    private int enableTab;

    public FilePickerOptions(){

    }

    private FilePickerOptions(Orientation orientation, int singlePick, int fileLimit, FileUpdateMethod updateMethod,
                              int enableDeepScan, int[] colorScheme, String hint, List<String> fileFilters, int enableTab) {
        this.orientation = orientation;
        this.singlePick = singlePick;
        this.fileLimit = fileLimit;
        this.updateMethod = updateMethod;
        this.enableDeepScan = enableDeepScan;
        this.colorScheme = colorScheme;
        this.hint = hint;
        this.fileFilters = fileFilters;
        this.enableTab = enableTab;
    }

    public boolean isSinglePick() {
        return singlePick == 1;
    }

    public int getFileLimit() {
        return fileLimit;
    }

    public FileUpdateMethod getUpdateMethod() {
        return updateMethod;
    }

    public boolean isEnableDeepScan() {
        return enableDeepScan == 1;
    }

    public int[] getColorScheme(){
        return colorScheme;
    }

    public String getHint(){
        return hint;
    }

    public List<String> getFileFilters(){
        return fileFilters;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isTabEnabled(){
        return enableTab == 1;
    }

    protected FilePickerOptions(Parcel in) {
        String or = in.readString();
        switch (or){
            case "PORTRAIT":
                this.orientation = Orientation.PORTRAIT;
                break;
            case "LANDSCAPE":
                this.orientation = Orientation.LANDSCAPE;
                break;
            default:
                this.orientation = Orientation.DEFAULT;
        }
        this.singlePick = in.readInt();
        this.fileLimit = in.readInt();
        String um = in.readString();
        switch (um){
            case "STREAM":
                this.updateMethod = FileUpdateMethod.STREAM;
                break;
            case "BUFFER":
                this.updateMethod = FileUpdateMethod.BUFFER;
                break;
            default:
                this.updateMethod = FileUpdateMethod.BUFFER;
        }

        this.enableDeepScan = in.readInt();
        this.colorScheme = in.createIntArray();
        this.hint = in.readString();
        in.readStringList(fileFilters);
        this.enableTab = in.readInt();
    }

    public static final Creator<FilePickerOptions> CREATOR = new Creator<FilePickerOptions>() {
        @Override
        public FilePickerOptions createFromParcel(Parcel in) {
            return new FilePickerOptions(in);
        }

        @Override
        public FilePickerOptions[] newArray(int size) {
            return new FilePickerOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orientation.getOrientation());
        parcel.writeInt(singlePick);
        parcel.writeInt(fileLimit);
        parcel.writeString(updateMethod.getUpdateMethod());
        parcel.writeInt(enableDeepScan);
        parcel.writeIntArray(colorScheme);
        parcel.writeString(hint);
        parcel.writeStringList(fileFilters);
        parcel.writeInt(enableTab);
    }

    public static class Build {
        private Orientation orientation = Orientation.DEFAULT;
        private FileUpdateMethod updateMethod = FileUpdateMethod.BUFFER;
        private boolean singlePick = false;
        private int fileLimit = 1;
        private boolean enableDeepScan = true;
        private int[] colorScheme = new int[0];
        private String hint = "Select File";
        private List<String> fileFilters = new ArrayList<>();
        private boolean enableTab = false;

        /**
         * Set preferred orientation for library to use.
         * If not set, default will follow the phone configuration
         * @param orientation
         * @return
         */
        public Build setOrientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        /**
         * Enable built in file manager button for traditional file browsing.
         * Will automatically set to disabled when multi file pick is enabled.
         * @param enable
         * @return
         */
        public Build setSinglePick(boolean enable){
            this.singlePick = enable;
            return this;
        }

        /**
         * Set limit number for picking up a file.
         * If not set, default is 1 which means a single file pick is proceeding instead of multi pick
         * @param limit
         * @return
         */
        public Build setFileLimit(int limit){
            this.fileLimit = limit;
            return this;
        }

        /**
         * Set preferred way for ui to update the file list
         * Set stream to subsequently update the file list when file scanner found any target file during the background task
         * Set buffer to wait until file scanner finished to scan the entire file in storage and then update the file list
         * @param updateMethod
         * @return
         */
        public Build setFileUpdateMethod(FileUpdateMethod updateMethod){
            this.updateMethod = updateMethod;
            return this;
        }

        /**
         * Enable this option to scan the entire storage directories indiscriminately
         * This including Android, cache, hidden directory, etc
         * Be aware that this will cause the progress run slower
         * @param enable
         * @return
         */
        public Build enableDeepScan(boolean enable){
            this.enableDeepScan = enable;
            return this;
        }

        /**
         * Set color scheme for specific view in picker activity
         * Will receive array of color that represents standard ui component ( ex toolbar, hint, icon, etc ) color
         * There is no limit for the array size but the given configuration will be adjusted accordingly to the ui source displayed at the moment
         * @param colorScheme
         * @return
         */
        public Build setColorScheme(int... colorScheme){
            this.colorScheme = colorScheme;
            return this;
        }

        public Build setHint(String hint){
            this.hint = hint;
            return this;
        }

        public Build addFileFilter(String filter){
            fileFilters.add(filter);
            return this;
        }

        public Build addFileFilter(String... filter){
            fileFilters.addAll(Arrays.asList(filter));
            return this;
        }

        public Build addFileFilter(FileTypePreset preset){
            fileFilters.addAll(Arrays.asList(preset.getPeset()));
            return this;
        }

        public Build enableTab(boolean enable){
            this.enableTab = enable;
            return this;
        }

        public FilePickerOptions build(){
            int isSinglePick = (this.singlePick || fileLimit < 2) ? 1 : 0;
            int deepScan = enableDeepScan ? 1 : 0;
            int tab = enableTab ? 1 : 0;
            if(enableTab)
                updateMethod = FileUpdateMethod.BUFFER;
            return new FilePickerOptions(orientation,
                    isSinglePick,
                    fileLimit,
                    updateMethod,
                    deepScan,
                    colorScheme,
                    hint,
                    fileFilters,
                    tab);
        }

    }
}
