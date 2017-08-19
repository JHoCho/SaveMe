package com.example.jaeho.myapplication.Controler;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by jaeho on 2017. 8. 20..
 */

public class LocationClass {

    private Geocoder geocoder;
    private Geocoder geocoder2;
    public LocationClass(Context context){
        geocoder = new Geocoder(context, Locale.KOREA);
        geocoder2 = new Geocoder(context,Locale.ENGLISH);
    }
    public String[] getLocationName(double lat,double lng){
        String name = null;
        String[] arr =new String[2];
        try{
            List<Address> list =  geocoder.getFromLocation(lat,lng,10);
            name = list.get(0).getAdminArea() //인천
                    +"_"+
                    list.get(0).getLocality()//중구
            ;
            arr[0] = name;
            list =  geocoder2.getFromLocation(lat,lng,10);

            name = list.get(0).getLocality() //인천
                    +"_"+    list.get(0).getSubLocality();

            arr[1] = name;
        }catch (IOException e){

        }

        return arr;
    }
}