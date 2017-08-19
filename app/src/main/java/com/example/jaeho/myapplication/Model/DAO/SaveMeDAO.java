package com.example.jaeho.myapplication.Model.DAO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaeho.myapplication.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.jaeho.myapplication.Model.DO.*;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.example.jaeho.myapplication.Controler.NAuth.myInform;
import static com.example.jaeho.myapplication.utils.Constants.hidProgressDialog;
import static com.example.jaeho.myapplication.utils.Constants.showProgressDialog;
import static com.example.jaeho.myapplication.utils.Constants.tostost;

/**
 * Created by jaeho on 2017. 8. 18..
 */

public abstract class SaveMeDAO implements ISaveMeDAO {
    DatabaseReference mRootRef;
    DatabaseReference mRef;//이곳에 해당 참조의 변화를 감지하는 addValueEventListener 등을 만들어 변화가 있는지 감시할 수 있다
    DatabaseReference mUserRef;
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
    public void checkSignUp(String id){
        mUserRef = mRootRef.child("saveme/user/"+id);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    final String tmpEmail=myInform.getEmail();
                    myInform.setEmail(null);
                    System.out.println("myinform:"+myInform.getName());
                    final EditText edt = new EditText(context);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context)
                            .setTitle("이름 설정해주세요")
                            .setMessage("이름써주세요")
                            .setView(edt);//아부분이 아마 콘텍스트가 메인으로 안가고끝나버린 LoginActivity로 가서 망가질듯
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myInform.setName(edt.getText().toString());
                            mUserRef.setValue(myInform);
                            myInform.setEmail(tmpEmail);
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((AppCompatActivity)context).finish();
                        }
                    });
                    dlg.setNegativeButton("취소",null);
                    dlg.show();
                    //mUserRef.setValue();
                }else {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    ((AppCompatActivity)context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
