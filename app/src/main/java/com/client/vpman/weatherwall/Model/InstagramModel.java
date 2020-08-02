package com.client.vpman.weatherwall.Model;

public class InstagramModel
{
    private String displayUrl,thumbnail_src,photoUrl;

    public InstagramModel(String displayUrl, String thumbnail_src, String photoUrl) {
        this.displayUrl = displayUrl;
        this.thumbnail_src = thumbnail_src;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getThumbnail_src() {
        return thumbnail_src;
    }

    public void setThumbnail_src(String thumbnail_src) {
        this.thumbnail_src = thumbnail_src;
    }
}
