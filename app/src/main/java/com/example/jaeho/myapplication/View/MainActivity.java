package com.example.jaeho.myapplication.View;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.jaeho.myapplication.Controler.ScreenService;
import com.example.jaeho.myapplication.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jaeho.myapplication.utils.Constants.JSON;

public class MainActivity extends AppCompatActivity {
    Button helpBtn;
    Switch holdSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helpBtn = (Button)findViewById(R.id.helpBtn);
        holdSwitch = (Switch)findViewById(R.id.holdSwitch);
        FirebaseMessaging.getInstance().subscribeToTopic("news");


        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                FirebaseMessaging.getInstance().subscribeToTopic("news");


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            OkHttpClient client = new OkHttpClient();
                            JSONObject json=new JSONObject();
                            JSONObject dataJson=new JSONObject();
                            dataJson.put("body","위험에 빠졌어요! 도와주세요!222222222!");
                            dataJson.put("title","※ Save Me Plz ※");
                            json.put("data",dataJson);
                            json.put("to","/topics/news");
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
                        }
                    }
                }).run();
            }
        });
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
}
