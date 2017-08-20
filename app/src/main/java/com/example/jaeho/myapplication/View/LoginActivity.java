package com.example.jaeho.myapplication.View;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaeho.myapplication.Model.DAO.ISaveMeDAO;
import com.example.jaeho.myapplication.Model.DAO.NowUsingDAO;
import com.example.jaeho.myapplication.R;
import com.google.gson.Gson;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.example.jaeho.myapplication.Controler.NAuth.*;
import static com.example.jaeho.myapplication.utils.Constants.MESSAGE_DONE;

public class LoginActivity extends AppCompatActivity {
    OAuthLoginButton loginBtn;
    TextView joinText;
    private static Context mContext;
    NMapLocationManager nMapLocationManager;
    NGeoPoint nGeoPoint = new NGeoPoint();


    ISaveMeDAO myDao = new NowUsingDAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;

        initData();
        loginBtn = (OAuthLoginButton) findViewById(R.id.loginBtn);
        mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);//들어가자마자 토큰검사
    }



    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {// 성공시 토큰 설정하고 AsyncT로 토큰값 저장
                initToken();
                new AsyncT().execute();

            } else {//실패시 로그인 창
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initToken() {
        String accessToken = mOAuthLoginModule.getAccessToken(mContext);
        String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
        long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
        String tokenType = mOAuthLoginModule.getTokenType(mContext);
        mOauthAT = accessToken;
        mOauthRT = refreshToken;
        mOauthExpires = String.valueOf(expiresAt);
        mOauthTokenType = tokenType;
        mOauthURL = "https://openapi.naver.com/v1/nid/me";
        mOAuthState = mOAuthLoginModule.getState(mContext).toString();
    }

    final Handler mHandler = new Handler(Looper.getMainLooper()){//메인쓰레드로 처리할 것임. 워커쓰레드로 하면 좋으나 아직 사용법을 모름
        public void handleMessage(Message m){
            switch (m.what){
                case MESSAGE_DONE:
                    controlString(m.obj.toString());
                    break;
            }
        }
    };
    private void initData() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private class AsyncT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String token = mOauthAT;
            System.out.println(token + "token");
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());

                Message message = mHandler.obtainMessage(MESSAGE_DONE);
                message.obj = response.toString();
                mHandler.sendMessage(message);
                //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                System.out.println(e);
                //Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;

        }
    }
    private void controlString(String s){
        Map map = new Gson().fromJson(s,HashMap.class);
        if(map.get("resultcode").equals("00")){
            Map childMap = (Map)map.get("response");

            Date d = new Date();
            SimpleDateFormat formatType = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            myInform.setTime(formatType.format(d));

            myInform.setLat(nGeoPoint.getLatitude());
            myInform.setLng(nGeoPoint.getLongitude());
            StringTokenizer stk = new StringTokenizer(String.valueOf(childMap.get("email")),"@");
            myInform.setEmail(stk.nextToken());
            myInform.setLocation("sssss");
            myInform.setAge(String.valueOf(childMap.get("age")));
            //myInform.setName(String.valueOf(childMap.get("name")));
            myDao.checkSignUp(myInform.getEmail(),getIntent());
        }
    }
}
