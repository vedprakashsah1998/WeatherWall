package com.client.vpman.weatherwall.Model;

public class ModelData3
{
    private String large2x,photographer,large;



    public ModelData3(String large2x, String photographer, String large) {
        this.large2x = large2x;
        this.photographer = photographer;
        this.large = large;
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
