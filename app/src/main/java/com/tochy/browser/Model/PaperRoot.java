package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaperRoot {

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

        @SerializedName("password")
        private String password;

        @SerializedName("access")
        private String access;

        @SerializedName("app_key")
        private String appKey;

        @SerializedName("package")
        private String jsonMemberPackage;

        @SerializedName("id")
        private int id;

        @SerializedName("username")
        private String username;

        public String getPassword() {
            return password;
        }

        public String getAccess() {
            return access;
        }

        public String getAppKey() {
            return appKey;
        }

        public String getJsonMemberPackage() {
            return jsonMemberPackage;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }
}