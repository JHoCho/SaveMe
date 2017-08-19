package com.example.jaeho.myapplication.Model.DO;

/**
 * Created by jaeho on 2017. 8. 18..
 */

public class EmergencyDO {
    private String id;
    private String key;
    private long time;
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}