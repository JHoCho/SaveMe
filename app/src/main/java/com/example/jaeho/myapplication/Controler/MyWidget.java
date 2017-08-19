package com.example.jaeho.myapplication.Controler;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.jaeho.myapplication.R;

/**
 * Created by jaeho on 2017. 8. 17..
 */

public class MyWidget extends AppWidgetProvider {
    //위젯의 기능을 정의한 클래
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i=0;i<appWidgetIds.length;i++)
        {
            int appId=appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            appWidgetManager.updateAppWidget(appWidgetIds,views);

        }
    }
}
