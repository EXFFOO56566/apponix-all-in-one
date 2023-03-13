package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertisementRoot {

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

        @SerializedName("ad_id")
        private int adId;

        @SerializedName("native")
        private String jsonMemberNative;

        @SerializedName("inter")
        private String inter;

        @SerializedName("ad_name")
        private String adName;

        @SerializedName("banner")
        private String banner;

        @SerializedName("status")
        private String status;

        public int getAdId() {
            return adId;
        }

        public String getJsonMemberNative() {
            return jsonMemberNative;
        }

        public String getInter() {
            return inter;
        }

        public String getAdName() {
            return adName;
        }

        public String getBanner() {
            return banner;
        }

        public String getStatus() {
            return status;
        }
    }
}