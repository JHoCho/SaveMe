package com.example.jaeho.myapplication.Model.DAO;

/**
 * Created by jaeho on 2017. 8. 18..
 */

public interface ISaveMeDAO {
    void makeAccount(final String id,final String pw);
    void addEmergency(double lat,double lng,String id);
    void addLocation(double lat,double lng);
    void checkSignUp(String id);
}
