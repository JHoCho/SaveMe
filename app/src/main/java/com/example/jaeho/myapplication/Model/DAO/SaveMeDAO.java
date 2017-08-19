package com.example.jaeho.myapplication.Model.DAO;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.jaeho.myapplication.Model.DO.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.example.jaeho.myapplication.utils.Constants.hidProgressDialog;
import static com.example.jaeho.myapplication.utils.Constants.showProgressDialog;
import static com.example.jaeho.myapplication.utils.Constants.tostost;

/**
 * Created by jaeho on 2017. 8. 18..
 */

public abstract class SaveMeDAO implements ISaveMeDAO {
    DatabaseReference mRootRef;
    DatabaseReference mRef;//이곳에 해당 참조의 변화를 감지하는 addValueEventListener 등을 만들어 변화가 있는지 감시할 수 있다
    Context context;
    public SaveMeDAO(Context context){
        this.context = context;
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public void makeAccount(String id, String pw) {

    }
    @Override
    public void addEmergency(double lat, double lng,String id) {
        //emergency에 id값을 더해주며 user/id 에 lat,lng,time수정
        mRef = mRootRef.child("saveme");
        EmergencyDO doo = new EmergencyDO();
        doo.setId(id);
        SaveMeDO saveMeDO = new SaveMeDO();
        saveMeDO.setLng(lng);
        Date d = new Date();
        SimpleDateFormat formatType = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("lat",dtoS(lat));
        hashMap.put("lng",dtoS(lng));
        hashMap.put("time",formatType.format(d));
        showProgressDialog(context);
        mRef.child("emergency").push().setValue(doo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    tostost("신고에 성공하였습니다.",context);
                } else {
                    tostost("신고에 실패하였습니다.",context);
                }
                hidProgressDialog();
            }
        });
        showProgressDialog(context);
        mRef.child("user").child(id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    tostost("user정보 수정에 성공하였습니다.",context);
                } else {
                    tostost("user정보 수정에 실패하였습니다.",context);
                }
                hidProgressDialog();
            }
        });
    }
    public String dtoS(double a){
        return Double.toString(a);
    }
    @Override
    public void addLocation(double lat, double lng) {

    }
}
