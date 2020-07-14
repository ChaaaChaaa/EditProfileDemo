package com.practice.edituserprofiledemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PImageData {

    @SerializedName("storedPath")
    private List<PathEntity> storedPath;


    public List<PathEntity> getStoredPath() {
        return storedPath;
    }

    public void setStoredPath(List<PathEntity> storedPath) {
        this.storedPath = storedPath;
    }

    public static class PathEntity {
        private String originPath;
        private String thumbPath;

        public String getOriginPath() {
            return originPath;
        }

        public void setOriginPath(String originPath) {
            this.originPath = originPath;
        }

        public String getThumbPath() {
            return thumbPath;
        }

        public void setThumbPath(String thumbPath) {
            this.thumbPath = thumbPath;
        }
    }
}

