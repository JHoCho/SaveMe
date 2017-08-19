package com.example.jaeho.myapplication.Controler;

/**
 * Created by jaeho on 2017. 8. 20..
 */

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import static com.example.jaeho.myapplication.utils.Constants.myLat;
import static com.example.jaeho.myapplication.utils.Constants.myLng;
import static com.example.jaeho.myapplication.utils.Constants.nowLocation;
import static com.example.jaeho.myapplication.utils.Constants.nowLoginId;

public class GPSUpdateService extends Service implements Runnable {
    boolean flag = true;
    public GPSUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Thread thread = new Thread(this);
        thread.start();
        return  START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void run() {
        LocationClass locationClass = new LocationClass(GPSUpdateService.this);

        while(flag){
            //좌표

            nowLocation =  locationClass.getLocationName(myLat,myLng)[1]; //좌표데이터 수신해야함.

            //파이어베이스 로케이션 데이터
            //위치 잡힌거랑 비교
            //다르면 기존에 있던걸 구독 취소
            //위치 새로 잡힌거를 구독하면된다.
            final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("saveme/user/"+nowLoginId);
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        HashMap map = (HashMap)dataSnapshot.getValue();
                        String location = (String)map.get("location");
                        //파이어베이스에 등록된 위치데이터
                        //내가 가지고 있는 위치 데이터를 찾아야함.
                        if(!nowLocation.equals(location)){
                            FirebaseMessaging.getInstance().subscribeToTopic(nowLocation);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(location);
                            dr.child("location").setValue(nowLocation);
                            Toast.makeText(GPSUpdateService.this, "위치가 바껴서 수정함", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(GPSUpdateService.this, "서비스 작동중임", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            try{
                Thread.sleep(600000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }
}