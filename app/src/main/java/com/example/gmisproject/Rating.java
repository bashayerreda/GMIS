package com.example.gmisproject;

public class Rating {
    private String username;
    private String email;
    private String rate;
    private String report;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Rating(String report) {
        this.report = report;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rate;
    }

    public void setRating(String rating) {
        this.rate = rating;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Rating(String username, String email, String rate, String report) {
        this.username = username;
        this.email = email;
        this.rate = rate;
        this.report = report;
    }
}




