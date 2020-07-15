package com.practice.edituserprofiledemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PImageData {

    @SerializedName("storedPath")
    private String storedPath;

    public String getStoredPath() {
        return storedPath;
    }

    public void setStoredPath(String storedPath) {
        this.storedPath = storedPath;
    }

}

