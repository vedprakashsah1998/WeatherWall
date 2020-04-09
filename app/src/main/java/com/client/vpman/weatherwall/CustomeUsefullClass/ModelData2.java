package com.client.vpman.weatherwall.CustomeUsefullClass;

public class ModelData2
{
    private String large2x,photographer,large,original,photoUrl;

    public ModelData2() {
    }
/*
    public ModelData2(String large2x, String photographer, String large, String original) {
        this.large2x = large2x;
        this.photographer = photographer;
        this.large = large;
        this.original = original;
    }*/

    public ModelData2(String large2x, String photographer, String large, String original, String photoUrl) {
        this.large2x = large2x;
        this.photographer = photographer;
        this.large = large;
        this.original = original;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getLarge2x() {
        return large2x;
    }

    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }
}
