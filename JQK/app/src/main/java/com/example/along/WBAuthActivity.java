package com.example.along;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.along.jqk.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/23.
 */
public class WBAuthActivity extends Activity {
    private static final String TAG = "WBAuthActivity";

    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
        mTokenText= (TextView) findViewById(R.id.token_text_view);
        TextView hintView= (TextView) findViewById(R.id.obtain_token_hint);
        hintView.setMovementMethod(new ScrollingMovementMethod());
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo=new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler=new SsoHandler(WBAuthActivity.this,mAuthInfo);
        // SSO 授权, 仅Web
        /*findViewById(R.id.test_sso_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorizeWeb(new AuthListener());
            }
        });*/
        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        findViewById(R.id.test_sso_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });
        findViewById(R.id.obtain_token_hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WBAuthActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            //从bundle中解析token
            mAccessToken= Oauth2AccessToken.parseAccessToken(bundle);
            Log.i("AuthListener","worked");
            //从这里获取用户输入的电话号码信息
            String phoneNum=mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()){
                updateTokenView(false);
                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this,mAccessToken);
                Toast.makeText(WBAuthActivity.this,"保存成功",Toast.LENGTH_LONG).show();
            }else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = bundle.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(WBAuthActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBAuthActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthActivity.this,
                    "取消", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    @TargetApi(Build.VERSION_CODES.N)
    private void updateTokenView(boolean hasExisted){
        String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new Date(mAccessToken.getExpiresTime()));
        String format=getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format,mAccessToken.getToken(),date));
        String message=String.format(format,mAccessToken.getToken(),date);
        if(hasExisted){
            message=getString(R.string.weibosdk_demo_token_has_existed)+"\n"+message;
        }
        mTokenText.setText(message);
    }
}
