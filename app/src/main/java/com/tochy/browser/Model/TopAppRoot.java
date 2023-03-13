package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAppRoot {

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

        @SerializedName("app_name")
        private String appName;

        @SerializedName("app_icon")
        private String appIcon;

        @SerializedName("app_url")
        private String appUrl;

        @SerializedName("top")
        private String top;

        public String getAppName() {
            return appName;
        }

        public String getAppIcon() {
            return appIcon;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public String getTop() {
            return top;
        }
    }
}