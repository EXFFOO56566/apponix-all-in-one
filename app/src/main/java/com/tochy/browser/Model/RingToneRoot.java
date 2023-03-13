package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RingToneRoot {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public List<DataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static class DataItem {

        @SerializedName("ring_name")
        private String ringName;

        @SerializedName("ringtone")
        private String ringtone;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        public String getRingName() {
            return ringName;
        }

        public String getRingtone() {
            return ringtone;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }
    }
}