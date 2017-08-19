package com.example.jaeho.myapplication.Controler;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.jaeho.myapplication.R;
import com.example.jaeho.myapplication.View.LockScreenActivity;

import java.util.zip.Inflater;

/**
 * Created by jaeho on 2017. 8. 17..
 */
//두번째로 필요한 친구로 화면이 꺼졌을때 ACTION_SCREEN_OFF인텐트를 받을 친구
    //화면이 켜질때는 ACTION_SCREEN_ON이라는 인텐트가 브로드케스트 꺼질때는 OFF로 저 브로드 캐스트 메세지를 받으면
    //그이후 LockScreenActivity를 띄워주면 됩니다.
public class ScreenReceiver extends BroadcastReceiver {
    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyguardLock=null;
    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;

    public static int a=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            if(km==null){
                km = (KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
            }
            if(keyguardLock==null){
                keyguardLock = km.newKeyguardLock(context.KEYGUARD_SERVICE);
            }

            if(telephonyManager == null){
                telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

            if(isPhoneIdle) {
                if (a == 0) {
                    disableKeyguard();
                    Intent i = new Intent(context, LockScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    a = 1;
                }
            }
        }

    }

    public void reenableKeyguard() {
        System.out.println("reenableKeyGuard시작");
        keyguardLock.reenableKeyguard();
    }

    public void disableKeyguard() {
        keyguardLock.disableKeyguard();
    }

    private PhoneStateListener phoneListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE :
                    isPhoneIdle = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING :
                    isPhoneIdle = false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK :
                    isPhoneIdle = false;
                    break;
            }
        }
    };


}
