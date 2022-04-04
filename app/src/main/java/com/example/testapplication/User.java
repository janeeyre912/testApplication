package com.example.testapplication;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String dob;
    private String gender;
    private double weight;

    public User(String fullName, String dob, String gender, double weight) {
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.weight = weight;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "User {fullName: " + fullName + ", date of birth: " + dob + ", gender: " + gender + ", weight: " + weight + "}";
    }
}
