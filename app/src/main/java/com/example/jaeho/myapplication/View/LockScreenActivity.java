package com.example.jaeho.myapplication.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.example.jaeho.myapplication.Controler.ScreenReceiver;
import com.example.jaeho.myapplication.Controler.ScreenService;
import com.example.jaeho.myapplication.R;

public class LockScreenActivity extends AppCompatActivity {
    SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int i =0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                this.i = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(i>90){
                    finish();
                    ScreenReceiver.a= 0;
                }else {
                    seekBar.setProgress(0);
                }
            }
        });
        //잠금화면 으로 사용할 엑티비티
    }
}
