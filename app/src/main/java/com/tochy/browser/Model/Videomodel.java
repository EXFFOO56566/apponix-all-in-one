package com.tochy.browser.Model;

public class Videomodel {
    private String vidName;
    private String vidPath;
    private String thumbPath;
    private String vidSize;
    private String vidUri;
    private Boolean selected = false;
    private String path;
    private String thumbpath;
    private String FolderName;
    private int numberOfVid = 0;
    private String firstVid;

    public String getVidName() {
        return vidName;
    }

    public void setVidName(String vidName) {
        this.vidName = vidName;
    }

    public String getVidPath() {
        return vidPath;
    }

    public void setVidPath(String vidPath) {
        this.vidPath = vidPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getVidSize() {
        return vidSize;
    }

    public void setVidSize(String vidSize) {
        this.vidSize = vidSize;
    }

    public String getVidUri() {
        return vidUri;
    }

    public void setVidUri(String vidUri) {
        this.vidUri = vidUri;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbpath() {
        return thumbpath;
    }

    public void setThumbpath(String thumbpath) {
        this.thumbpath = thumbpath;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public int getNumberOfVid() {
        return numberOfVid;
    }

    public void setNumberOfVid(int numberOfVid) {
        this.numberOfVid = numberOfVid;
    }

    public String getFirstVid() {
        return firstVid;
    }

    public void setFirstVid(String firstVid) {
        this.firstVid = firstVid;
    }

    public void addvideosCount() {
    }
}
