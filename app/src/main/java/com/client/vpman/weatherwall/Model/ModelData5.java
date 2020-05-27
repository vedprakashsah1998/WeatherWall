package com.client.vpman.weatherwall.Model;

public class ModelData5
{
    private String large2x,photographer,large,original,photoUrl;



    public ModelData5(String large2x, String photographer, String large, String original, String photoUrl) {
        this.large2x = large2x;
        this.photographer = photographer;
        this.large = large;
        this.original = original;
        this.photoUrl = photoUrl;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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
