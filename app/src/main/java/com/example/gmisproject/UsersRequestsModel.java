package com.example.gmisproject;

public class UsersRequestsModel {

    private String id, full_name,
            address, bin_number,
            phone_number, payment;

    public UsersRequestsModel(String id, String full_name, String address, String bin_number, String phone_number, String payment) {
        this.id = id;
        this.full_name = full_name;
        this.address = address;
        this.bin_number = bin_number;
        this.phone_number = phone_number;
        this.payment = payment;
    }

    public UsersRequestsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBin_number() {
        return bin_number;
    }

    public void setBin_number(String bin_number) {
        this.bin_number = bin_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}


