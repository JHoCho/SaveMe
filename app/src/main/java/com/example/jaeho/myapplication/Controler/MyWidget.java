package com.example.jaeho.myapplication.Controler;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.jaeho.myapplication.R;
import com.example.jaeho.myapplication.View.LoginActivity;
import com.example.jaeho.myapplication.View.MainActivity;

import java.util.Random;

import static com.example.jaeho.myapplication.utils.Constants.tostost;

/**
 * Created by jaeho on 2017. 8. 17..
 */

public class MyWidget extends AppWidgetProvider {
    //위젯의 기능을 정의한 클래스
    private static final String ACTION_CLICK = "ACTION_CLICK";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, MyWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for(int widgetId : allWidgetIds){
            //int number = (new Random().nextInt(100));
           // MainActivity.report2();
        }

      //  super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
}
