package com.example.jaeho.myapplication.View;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.jaeho.myapplication.Controler.ScreenReceiver;
import com.example.jaeho.myapplication.Controler.ScreenService;
import com.example.jaeho.myapplication.R;

public class LockScreenActivity extends AppCompatActivity {
    SeekBar seekBar;
    ImageButton holdHelpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        holdHelpBtn = (ImageButton) findViewById(R.id.holdHelpBtn);
        holdHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.report(LockScreenActivity.this);
            }
        });
        setSeekbar(seekBar, getResources());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int i = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                this.i = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (i > 90) {
                    finish();
                    ScreenReceiver.a = 0;
                } else {
                    seekBar.setProgress(0);
                }
            }
        });
    }

    //잠금화면 으로 사용할 엑티비티
    public static void setSeekbar(final SeekBar seekBar, final Resources res) {
        seekBar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                if (seekBar.getHeight() > 0) {
                    Drawable thumb = res.getDrawable(R.drawable.holdthumb);
                    int h = seekBar.getMeasuredHeight() + 30;
                    int w = h;
                    Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
                    Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h - 25, true);
                    Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                    newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                    seekBar.setThumb(newThumb);
                    seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }
}


