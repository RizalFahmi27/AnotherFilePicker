package com.github.rzilyn.multifilepicker.utils;

/**
 * Created by Rizal Fahmi on 17-Dec-17.
 */

public enum FileUpdateMethod {
    STREAM ("STREAM"),
    BUFFER ("BUFFER");

    private final String UPDATE_METHOD;

    private FileUpdateMethod(String s){
        this.UPDATE_METHOD = s;
    }

    public String getUpdateMethod(){
        return UPDATE_METHOD;
    }
}
