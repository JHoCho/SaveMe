package com.example.jaeho.myapplication.Controler;

import com.example.jaeho.myapplication.Model.DO.SaveMeDO;
import com.nhn.android.naverlogin.OAuthLogin;

/**
 * Created by jaeho on 2017. 8. 19..
 */

public class NAuth {
    public static final String OAUTH_CLIENT_ID = "EzpY2nBNeLRnW5p6Z50T";
    public static final String OAUTH_CLIENT_SECRET = "yuFYgzOgjC";
    public static final String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
    public static OAuthLogin mOAuthLoginModule;
    public static String mOauthAT,mOauthRT,mOauthExpires,mOauthTokenType,mOAuthState,mOauthURL;
    public static SaveMeDO myInform = new SaveMeDO();
}
