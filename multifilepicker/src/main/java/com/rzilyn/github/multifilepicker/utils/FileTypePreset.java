package com.rzilyn.github.multifilepicker.utils;

/**
 * Created by Rizal Fahmi on 18-Dec-17.
 */

public enum FileTypePreset {

    DOCUMENTS(new String[]{"doc","docx","ppt","pptx","xls","xlsx","pdf","txt"}),

    IMAGES(new String[]{"jpg","jpeg","png","gif","ico"}),

    ARCHIVES(new String[]{"zip","rar","tar","gz","7zip","cab","dmg","apk"}),

    VIDEOS(new String[]{"avi","mp4","mpeg","mkv","mov","3gp","m4a","wmv","flv"}),

    AUDIOS(new String[]{"mp3","aac","ogg","wav","amr","flac","wma","webm"});

    private String[] PRESET;

    private FileTypePreset(String[] filePreset){
        this.PRESET = filePreset;
    }

    public String[] getPeset(){
        return PRESET;
    }

}
