package com.example.mapsandroid;

import com.google.gson.Gson;

public class Location {
    private String title, description;
    private float rating;
    private double lang, lat;

    public Location(String title, String description, float rating, double lat, double lang) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.lang = lang;
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
