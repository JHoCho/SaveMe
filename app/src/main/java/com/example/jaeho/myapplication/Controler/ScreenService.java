package com.example.jaeho.myapplication.Controler;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by jaeho on 2017. 8. 17..
 */

public class ScreenService extends Service {
    private ScreenReceiver mReceiver = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        //registerReceiver(mReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent!=null)
        {
            if (intent.getAction()==null)
            {
                if (mReceiver!=null)
                {
                    mReceiver=new ScreenReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver,filter);
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mReceiver!=null)
        {
            mReceiver.reenableKeyguard();
            unregisterReceiver(mReceiver);
        }
    }
}
