package com.example.gmisproject.user;

public class UserBinModel {
    private String binId;
    private String userAddress;
    private String binStatus;
    private int binPercentage;
    private int binImageId;

    public UserBinModel(String binId, String userAddress, String binStatus, int binPercentage, int binImageId) {
        this.binId = binId;
        this.userAddress = userAddress;
        this.binStatus = binStatus;
        this.binPercentage = binPercentage;
        this.binImageId = binImageId;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getBinStatus() {
        return binStatus;
    }

    public void setBinStatus(String binStatus) {
        this.binStatus = binStatus;
    }

    public int getBinPercentage() {
        return binPercentage;
    }

    public void setBinPercentage(int binPercentage) {
        this.binPercentage = binPercentage;
    }

    public int getBinImageId() {
        return binImageId;
    }

    public void setBinImageId(int binImageId) {
        this.binImageId = binImageId;
    }
}
