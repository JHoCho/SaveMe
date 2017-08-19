package com.example.jaeho.myapplication.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by jaeho on 2017. 8. 18..
 */

public class Constants {
    public static ProgressDialog prdlg;

    public static void showProgressDialog(Context context) {
        prdlg = ProgressDialog.show(context, "잠시만 기다려주세요", "서버와 통신중 입니다.", true);
    }
    public static void tostost(String s,Context context){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    public static void hidProgressDialog() {
        prdlg.dismiss();
    }

}
