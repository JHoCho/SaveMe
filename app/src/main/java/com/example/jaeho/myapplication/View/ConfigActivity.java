package com.example.jaeho.myapplication.View;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jaeho.myapplication.Controler.ScreenReceiver;
import com.example.jaeho.myapplication.Controler.ScreenService;
import com.example.jaeho.myapplication.R;

public class ConfigActivity extends AppCompatActivity {
    private Button onBtn, offBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        onBtn = (Button)findViewById(R.id.onBtn);
        offBtn = (Button)findViewById(R.id.offBtn);
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this,ScreenService.class);
                startService(intent);
            }
        });
        offBtn.setText("OFF");
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this,ScreenService.class);
                stopService(intent);
            }
        });
    }
}
