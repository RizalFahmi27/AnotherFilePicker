package com.rzilyn.github.multifilepicker.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.rzilyn.github.multifilepicker.FilePickerOptions;
import com.rzilyn.github.multifilepicker.R;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Rizal Fahmi on 15-Dec-17.
 */

public class Util {

    public static final String FILE_JPG = "jpg";
    public static final String FILE_JPEG = "jpeg";
    public static final String FILE_PNG = "png";
    public static final String FILE_SVG = "svg";
    public static final String FILE_GIF = "gif";
    public static final String FILE_ICO = "ico";

    public static final String FILE_MP3 = "mp3";
    public static final String FILE_OGG = "ogg";
    public static final String FILE_WAV = "wav";
    public static final String FILE_AMR = "amr";
    public static final String FILE_AAC = "aac";
    public static final String FILE_FLAC = "flac";
    public static final String FILE_M4A = "m4a";
    public static final String FILE_WMA = "wma";
    public static final String FILE_WEBM = "webm";

    public static final String FILE_AVI = "avi";
    public static final String FILE_MP4 = "mp4";
    public static final String FILE_3GP = "3gp";
    public static final String FILE_MKV = "mkv";
    public static final String FILE_FLV = "flv";
    public static final String FILE_VOB = "vob";
    public static final String FILE_MOV = "mov";
    public static final String FILE_WMV = "wmv";
    public static final String FILE_MPEG = "mpeg";

    public static final String FILE_DOC = "doc";
    public static final String FILE_DOCX = "docx";
    public static final String FILE_XLS = "xls";
    public static final String FILE_XLSX = "xlsx";
    public static final String FILE_PPT = "ppt";
    public static final String FILE_PPTX = "pptx";
    public static final String FILE_PDF = "pdf";
    public static final String FILE_TXT = "txt";

    public static final String FILE_ZIP = "zip";
    public static final String FILE_RAR = "rar";
    public static final String FILE_TAR = "tar";
    public static final String FILE_CAB = "cab";
    public static final String FILE_DMG = "dmg";
    public static final String FILE_APK = "apk";

    public static final String FILE_DUMMY = "dummy_null";

    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    private static final String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";

    private static final String[] fileSizePrefix = {"Byte","KB","MB","GB","TB","PB","EB","ZB","YB"};


    public static int getFileIconColor(String fileType){
        switch (fileType.toLowerCase()){
            case FILE_PDF:
            case FILE_RAR:
            case FILE_CAB:
            case FILE_WMV:
            case FILE_3GP:
            case FILE_OGG:
            case FILE_AAC:
            case FILE_SVG:
            case FILE_ICO:
            case FILE_AVI:
                return R.color.colorRed;
            case FILE_JPEG:
            case FILE_JPG:
            case FILE_GIF:
            case FILE_MP3:
            case FILE_WAV:
            case FILE_M4A:
            case FILE_WEBM:
            case FILE_DOC:
            case FILE_DOCX:
            case FILE_DMG:
                return R.color.colorBlue;
            case FILE_ZIP:
            case FILE_TAR:
            case FILE_TXT:
            case FILE_FLV:
            case FILE_VOB:
            case FILE_PNG:
            case FILE_AMR:
                return R.color.colorBrown;
            case FILE_PPT:
            case FILE_PPTX:
            case FILE_MP4:
            case FILE_MKV:
            case FILE_WMA:
                return R.color.colorOrange;
            case FILE_XLS:
            case FILE_XLSX:
            case FILE_MPEG:
            case FILE_MOV:
            case FILE_FLAC:
            case FILE_APK:
                return R.color.colorGreen;
            default:
                return R.color.colorGrey;
        }
    }

    public static Map<String,File> getStorageDirectories(Context context) {
            Map<String, File> storageLocations = new HashMap<>(10);
            File sdCard = Environment.getExternalStorageDirectory();
            storageLocations.put(SD_CARD, sdCard);
            final String rawSecondaryStorage = System.getenv(ENV_SECONDARY_STORAGE);
            if (!TextUtils.isEmpty(rawSecondaryStorage)) {
                String[] externalCards = rawSecondaryStorage.split(":");
                for (int i = 0; i < externalCards.length; i++) {
                    String path = externalCards[i];
                    storageLocations.put(EXTERNAL_SD_CARD + String.format(i == 0 ? "" : "_%d", i), new File(path));
                }
            }
            return storageLocations;
    }

    public static final String getFileExtension(String filename){
        String[] ext = filename.split("\\.");
        return ext[ext.length - 1];
    }

    public static final String getFileSizeString(double originalSize){
        return fileSize(0,originalSize);
    }

    private static String fileSize(int prefixPos, double size){
        if(size / (double) 1024 > 1){
            return fileSize(++prefixPos,size / 1024);
        }
        else {
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            return df.format(size) + " "+ fileSizePrefix[prefixPos];
        }
    }

    public static String getMimeType(Context context, Uri uri){
        String mimeType = null;

        if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        Log.d("Util","File mimetype : "+mimeType);
        return mimeType;
    }

    public static String parseDate(long date, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date d = new Date(date);
        return df.format(d);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public static <T> List<T> filter(List<T> origin, final Predicate<T> predicate){
        Collection<T> result = new ArrayList<>();
        for(T t : origin){
            if(predicate.test(t))
                result.add(t);
        }
        return new ArrayList<>(result);
    }

    public static <T> List<T> filter(List<T> origin, final com.android.internal.util.Predicate<T> predicate){
        Collection<T> result = new ArrayList<>();
        for(T t : origin){
            Log.d("Util","result size : "+origin.size());
            if(predicate.apply(t))
                result.add(t);
        }
        return new ArrayList<>(result);
    }

}
