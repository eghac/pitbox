package com.bzgroup.pitboxauxiliovehicular.entities;

public class Address {
    private String id;
    private String latitude;
    private String longitude;
    private String description;

    public Address(String id, String latitude, String longitude, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
