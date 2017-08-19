package com.example.jaeho.myapplication.utils;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import static com.example.jaeho.myapplication.utils.Constants.MY_PERMISSIONS_CHANGE_WIFI;
import static com.example.jaeho.myapplication.utils.Constants.MY_PERMISSIONS_REQUEST_ACCESS_WIFI;
import static com.example.jaeho.myapplication.utils.Constants.MY_PERMISSIONS_REQUEST_COARSE_LOCATION;
import static com.example.jaeho.myapplication.utils.Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

/**
 * Created by jaeho on 2017. 8. 20..
 */

public class PermissionActivity extends Fragment {
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //해당 퍼미션이 그렌트된 경우
                    Intent intent = new Intent(this.getActivity(),getClass());
                    startActivity(intent);
                }else{
                    // 퍼미션이 디나이된 경우
                    // 퍼미션이 디나이된 경우 프로그램의 권한부분으로 바로 넘겨 권한 설정을 유도.
                    // Log.d("Permission always denyed");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:"+ this.getActivity().getPackageName()));
                    startActivity(intent);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //해당 퍼미션이 그렌트된 경우
                    Intent intent = new Intent(this.getActivity(),getClass());
                    startActivity(intent);
                }else{
                    // 퍼미션이 디나이된 경우
                    // 퍼미션이 디나이된 경우 프로그램의 권한부분으로 바로 넘겨 권한 설정을 유도.
                    // Log.d("Permission always denyed");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:"+ this.getActivity().getPackageName()));
                    startActivity(intent);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_WIFI:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //해당 퍼미션이 그렌트된 경우
                    Intent intent = new Intent(this.getActivity(),getClass());
                    startActivity(intent);
                }else{
                    // 퍼미션이 디나이된 경우
                    // 퍼미션이 디나이된 경우 프로그램의 권한부분으로 바로 넘겨 권한 설정을 유도.
                    // Log.d("Permission always denyed");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:"+ this.getActivity().getPackageName()));
                    startActivity(intent);
                }
                return;
            }
            case MY_PERMISSIONS_CHANGE_WIFI:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //해당 퍼미션이 그렌트된 경우
                    Intent intent = new Intent(this.getActivity(),getClass());
                    startActivity(intent);
                }else{
                    // 퍼미션이 디나이된 경우
                    // 퍼미션이 디나이된 경우 프로그램의 권한부분으로 바로 넘겨 권한 설정을 유도.
                    // Log.d("Permission always denyed");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:"+ this.getActivity().getPackageName()));
                    startActivity(intent);
                }
                return;
            }
                //다른 퍼미션을 체크할 부분
        }
    }
}
