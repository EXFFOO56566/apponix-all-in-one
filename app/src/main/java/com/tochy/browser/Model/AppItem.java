package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

public class AppItem {

    @SerializedName("app_name")
    private String appName;

    @SerializedName("app_icon")
    private String appIcon;

    @SerializedName("app_url")
    private String appUrl;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("top")
    private String top;

    public AppItem(String appIcon, String appName, String appUrl) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appUrl = appUrl;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }


    @Override
    public String toString() {
        return "AppItem{" +
                "appName='" + appName + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", top='" + top + '\''
                ;
    }
}

