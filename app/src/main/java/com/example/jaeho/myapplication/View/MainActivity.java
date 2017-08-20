package com.example.jaeho.myapplication.View;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaeho.myapplication.Controler.GPSUpdateService;
import com.example.jaeho.myapplication.Controler.ScreenService;
import com.example.jaeho.myapplication.Model.DO.EmergencyDO;
import com.example.jaeho.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jaeho.myapplication.utils.Constants.JSON;
import static com.example.jaeho.myapplication.utils.Constants.MESSAGE_DONE;
import static com.example.jaeho.myapplication.utils.Constants.doSiDongName;
import static com.example.jaeho.myapplication.utils.Constants.hidProgressDialog;
import static com.example.jaeho.myapplication.utils.Constants.myLat;
import static com.example.jaeho.myapplication.utils.Constants.myLng;
import static com.example.jaeho.myapplication.utils.Constants.nowLocation;
import static com.example.jaeho.myapplication.utils.Constants.nowLoginId;
import static com.example.jaeho.myapplication.utils.Constants.showProgressDialog;

public class MainActivity extends AppCompatActivity {
    Button helpBtn;
    ImageButton gpsBtn;
    Switch holdSwitch;
    Context mapContext;
    TextView locationText;
    String id ;
    boolean flag = false;
    private final String CLIENT_ID = "EzpY2nBNeLRnW5p6Z50T";
    private NGeoPoint nGeoPoint;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        id = getIntent().getStringExtra("id");
        flag = getIntent().getBooleanExtra("flag",false);

        helpBtn = (Button)findViewById(R.id.helpBtn);
        locationText = (TextView)findViewById(R.id.locationText);
        gpsBtn = (ImageButton)findViewById(R.id.gpsBtn);
        holdSwitch = (Switch)findViewById(R.id.holdSwitch);
        final Fragment1 fragment1 = new Fragment1();
        if(flag){
            Toast.makeText(MainActivity.this, "플래그 수신", Toast.LENGTH_SHORT).show();
            fragment1.lat = getIntent().getDoubleExtra("lat",0);
            fragment1.lng = getIntent().getDoubleExtra("lng",0);
            fragment1.flag = flag;
        }
        fragment1.setArguments(new Bundle());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.NMapFrame,fragment1);
        fragmentTransaction.commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mapContext = fragment1.getContext();

        }

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               report(MainActivity.this);
            }
        });
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(doSiDongName!=null) {

                    locationText.setText(doSiDongName);
                }
                System.out.println(doSiDongName);
            }
        });
        if(doSiDongName!=null)
            locationText.setText(doSiDongName);
        holdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holdSwitch.isChecked()){
                    Intent intent = new Intent(MainActivity.this,ScreenService.class);
                    startService(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this,ScreenService.class);
                    stopService(intent);
                }
            }
        });
    }
    public static void report(final Context context){
        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("saveme/emergency");
        showProgressDialog(context);
        final DatabaseReference userDr = FirebaseDatabase.getInstance().getReference().child("saveme/user/"+nowLoginId);
        userDr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = (HashMap)dataSnapshot.getValue();
                long reportTime = (long)map.get("time");
                long time= System.currentTimeMillis();
                hidProgressDialog();
                if(time-reportTime<60000){
                    Toast.makeText(context, "신고 간격이 너무 빠릅니다 잠시후 시도해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference pushRf = dr.push();
                    String key = pushRf.getKey();

                    String message = "위험에 빠졌어요!";
                    EmergencyDO eDo = new EmergencyDO();
                    eDo.setId(nowLoginId);
                    eDo.setKey(key);
                    eDo.setMessage(message);
                    eDo.setTime(time);
                    pushRf.setValue(eDo);
                    userDr.child("time").setValue(time);
                    userDr.child("lat").setValue(myLat);
                    userDr.child("lng").setValue(myLng);
                    Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                OkHttpClient client = new OkHttpClient();
                                JSONObject json=new JSONObject();
                                JSONObject dataJson=new JSONObject();
                                dataJson.put("body","위험에 빠졌어요! 도와주세요!!");
                                dataJson.put("title","※ Save Me Plz ※");
                                dataJson.put("lat",myLat);
                                dataJson.put("lng",myLng);
                                json.put("data",dataJson);
                                json.put("to","/topics/"+nowLocation);
                                //Toast.makeText(mapContext, nowLocation, Toast.LENGTH_SHORT).show();
                                RequestBody body = RequestBody.create(JSON, json.toString());
                                Request request = new Request.Builder()
                                        .header("Authorization","key=AAAAP7CVeOM:APA91bHP5ALJ2vb7iREvP42sxXXoZm_jnoj1H6oZHv3tViSNxKPzZizvVrbdJfzN3VCRIH-QvSUJoP91h-nRbevt3VseHdT8m_gP8cm7iIj0MeeXG3LIDN8Ddx2Y8-G_iBw2mZl27ux-")
                                        .url("https://fcm.googleapis.com/fcm/send")
                                        .post(body)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String finalResponse = response.body().string();
                            }catch (Exception e){
                                //Log.d(TAG,e+"");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public static void report2(){
        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("saveme/emergency");
        //showProgressDialog(context);
        final DatabaseReference userDr = FirebaseDatabase.getInstance().getReference().child("saveme/user/"+nowLoginId);
        userDr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = (HashMap)dataSnapshot.getValue();
                long reportTime = (long)map.get("time");
                long time= System.currentTimeMillis();
//                hidProgressDialog();
                if(time-reportTime<60000){
             //       Toast.makeText(context, "신고 간격이 너무 빠릅니다 잠시후 시도해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference pushRf = dr.push();
                    String key = pushRf.getKey();

                    String message = "위험에 빠졌어요!";
                    EmergencyDO eDo = new EmergencyDO();
                    eDo.setId(nowLoginId);
                    eDo.setKey(key);
                    eDo.setMessage(message);
                    eDo.setTime(time);
                    pushRf.setValue(eDo);
                    userDr.child("time").setValue(time);
                    userDr.child("lat").setValue(myLat);
                    userDr.child("lng").setValue(myLng);
              //      Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                OkHttpClient client = new OkHttpClient();
                                JSONObject json=new JSONObject();
                                JSONObject dataJson=new JSONObject();
                                dataJson.put("body","위험에 빠졌어요! 도와주세요!!");
                                dataJson.put("title","※ Save Me Plz ※");
                                dataJson.put("lat",myLat);
                                dataJson.put("lng",myLng);
                                json.put("data",dataJson);
                                json.put("to","/topics/"+nowLocation);
                                //Toast.makeText(mapContext, nowLocation, Toast.LENGTH_SHORT).show();
                                RequestBody body = RequestBody.create(JSON, json.toString());
                                Request request = new Request.Builder()
                                        .header("Authorization","key=AAAAP7CVeOM:APA91bHP5ALJ2vb7iREvP42sxXXoZm_jnoj1H6oZHv3tViSNxKPzZizvVrbdJfzN3VCRIH-QvSUJoP91h-nRbevt3VseHdT8m_gP8cm7iIj0MeeXG3LIDN8Ddx2Y8-G_iBw2mZl27ux-")
                                        .url("https://fcm.googleapis.com/fcm/send")
                                        .post(body)
                                        .build();
                                Response response = client.newCall(request).execute();
                                String finalResponse = response.body().string();
                            }catch (Exception e){
                                //Log.d(TAG,e+"");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}
