package com.tochy.browser.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatagoryAppModel {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("meaasge")
    private String meaasge;

    @SerializedName("status")
    private int status;

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public String getMeaasge() {
        return meaasge;
    }

    public void setMeaasge(String meaasge) {
        this.meaasge = meaasge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

        private Boolean isSelected;

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }

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