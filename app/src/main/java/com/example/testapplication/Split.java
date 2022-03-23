package com.example.testapplication;

public class Split {
    double averageSpeed;
    double distance;
    String startTime;
    String endTime;

    Split(){
        averageSpeed = 0.0;
        distance = 0.0;
        startTime = "";
        endTime = "";
    }

    Split(double averageSpeed, double distance, String startTime, String endTime){
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    String getSplitInfo(){
        return averageSpeed + "\n"
                + distance + "\n"
                + startTime + "\n"
                + endTime + "\n";
    }
}
