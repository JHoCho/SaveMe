package com.example.jaeho.myapplication.Model.DO;


/**
 * Created by jaeho on 2017. 8. 18..
 */

public class SaveMeDO {
    private int age;
    private double lat;
    private double lng;
    private String location;
    private String name;
    private String time;
    public SaveMeDO(){}
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
