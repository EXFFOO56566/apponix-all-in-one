package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppsModel {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("meaasge")
    private String meaasge;

    @SerializedName("status")
    private int status;

    public List<DataItem> getData() {
        return data;
    }

    public String getMeaasge() {
        return meaasge;
    }

    public int getStatus() {
        return status;
    }


    public static class DataItem {

        @SerializedName("app")
        private List<AppItem> app;

        @SerializedName("category_name")
        private String categoryName;

        @SerializedName("category_icon")
        private String categoryIcon;

        @SerializedName("categorie_id")
        private int categorieId;

        public List<AppItem> getApp() {
            return app;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCategoryIcon() {
            return categoryIcon;
        }

        public int getCategorieId() {
            return categorieId;
        }
    }
}