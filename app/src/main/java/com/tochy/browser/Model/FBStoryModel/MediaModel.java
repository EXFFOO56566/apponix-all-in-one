package com.tochy.browser.Model.FBStoryModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MediaModel implements Serializable {
    @SerializedName("media")
    private MediaDataModel mediaDataModel;

    public MediaDataModel getMediaDataModel() {
        return mediaDataModel;
    }

    public void setMediaDataModel(MediaDataModel mediaDataModel) {
        this.mediaDataModel = mediaDataModel;
    }
}
