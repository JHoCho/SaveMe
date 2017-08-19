package com.example.jaeho.myapplication.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jaeho.myapplication.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Button selectBtn,helpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectBtn = (Button)findViewById(R.id.selectBtn);
        helpBtn = (Button)findViewById(R.id.helpBtn);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ConfigActivity.class);
                startActivity(i);
            }
        });
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,HelpMeActivity.class);
                startActivity(i);
            }
        });
    }
}
