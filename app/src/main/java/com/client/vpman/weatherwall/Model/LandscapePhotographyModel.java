package com.client.vpman.weatherwall.Model;

public class LandscapePhotographyModel
{
    private String displayUrl,thumbnail_src;

    public LandscapePhotographyModel(String displayUrl, String thumbnail_src) {
        this.displayUrl = displayUrl;
        this.thumbnail_src = thumbnail_src;
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
