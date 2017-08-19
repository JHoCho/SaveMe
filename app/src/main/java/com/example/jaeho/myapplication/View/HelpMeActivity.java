package com.example.jaeho.myapplication.View;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jaeho.myapplication.Model.DAO.ISaveMeDAO;
import com.example.jaeho.myapplication.Model.DAO.NowUsingDAO;
import com.example.jaeho.myapplication.R;
import com.nhn.android.maps.maplib.NGPoint;
import com.nhn.android.maps.maplib.NGeoPoint;

import static com.example.jaeho.myapplication.Controler.NAuth.myInform;
import static com.example.jaeho.myapplication.utils.Constants.showProgressDialog;

public class HelpMeActivity extends AppCompatActivity {
    private final String TAG = "HelpMeActivity";
    Button btn;
    ISaveMeDAO myDao = new NowUsingDAO(this);
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_me);
        btn = (Button)findViewById(R.id.btn);
        tv = (TextView)findViewById(R.id.tv);
        final NGeoPoint nGeoPoint = new NGeoPoint();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDao.addEmergency(nGeoPoint.getLatitude(),nGeoPoint.getLongitude(),myInform.getEmail());//이부분을 동적으로 바꿔야함.lat lng id
                tv.setText("도움을 요청중입니다.");
            }
        });
    }
}