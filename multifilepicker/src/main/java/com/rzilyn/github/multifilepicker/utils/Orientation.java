package com.rzilyn.github.multifilepicker.utils;

/**
 * Created by Rizal Fahmi on 13-Dec-17.
 */


public enum Orientation {
    DEFAULT("DEFAULT"),

    LANDSCAPE("LANDSCAPE"),

    PORTRAIT("PORTRAIT");

    private final String ORIENTATION;

    private Orientation(String o){
        this.ORIENTATION = o;
    }

    public String getOrientation(){
        return ORIENTATION;
    }
}