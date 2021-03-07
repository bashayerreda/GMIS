package com.example.gmisproject;

public class BinsModel {

    private String status, address, location;
    private int percentage, binId, binImageId;

    public BinsModel() {
    }

    public BinsModel(String status, String address, String location, int percentage, int binId) {
        this.status = status;
        this.address = address;
        this.location = location;
        this.percentage = percentage;
        this.binId = binId;
    }

    public BinsModel(String status, String address, int percentage, int binId, int binImageId) {
        this.status = status;
        this.address = address;
        this.percentage = percentage;
        this.binId = binId;
        this.binImageId = binImageId;
    }


    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getBinId() {
        return binId;
    }

    public int getBinImageId() {
        return binImageId;
    }
}