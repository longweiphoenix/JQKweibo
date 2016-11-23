package com.example.administrator.jqk;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/18.
 */
public class WeiBoHomeFragment extends Fragment implements View.OnClickListener{
    ImageView radar;
    TextView friend_focus,userName,radarPop,scan,taxi;
    ListView listView;
    LayoutInflater layoutInflater;
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wei_bo_home_fragment,null);
        radar= (ImageView) view.findViewById(R.id.radar);
        friend_focus= (TextView) view.findViewById(R.id.friend_focus);

        //添加微博内容
        listView= (ListView) view.findViewById(R.id.list_weibo);
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo=new AuthInfo(getActivity(),Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler=new SsoHandler(getActivity(),mAuthInfo);



        userName= (TextView) view.findViewById(R.id.user_name);
        layoutInflater=inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radar:
                createRadarPopupWindow();
                break;
            case R.id.friend_focus:
                Toast.makeText(getActivity(),"friend_focus----Started",Toast.LENGTH_SHORT).show();
                break;
            case R.id.list_weibo:
                break;
            case R.id.user_name:
                createMinePopupWindow();
                break;

        }
    }
    //创建雷达泡泡窗口
    public void createRadarPopupWindow(){
        radarPopupWindow=new PopupWindow(getActivity());
        View view=layoutInflater.inflate(R.layout.popupwindow_layout,null);
        radarPop= (TextView) view.findViewById(R.id.radar);
        radarPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"radarPopupWindow----Started",Toast.LENGTH_SHORT).show();
            }
        });
        scan= (TextView) view.findViewById(R.id.scan);
        taxi= (TextView) view.findViewById(R.id.taxi);
        radarPopupWindow.setContentView(view);
        radarPopupWindow.showAsDropDown(radar);
    }
    PopupWindow radarPopupWindow,userPopupWindow;
    //显示用户名下的泡泡窗口
    public void createMinePopupWindow(){
        userPopupWindow=new PopupWindow(getActivity());
        View view=layoutInflater.inflate(R.layout.user_popup_window,null);
        userPopupWindow.setContentView(view);
        userPopupWindow.showAsDropDown(userName);
    }
    public void addData(MyAdapter adapter){
        mSsoHandler.authorize(new AuthListener());
    }
    //异步线程获取数据
    public class WeiboAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("access_token",mAccessToken.getToken());
                connection.setRequestProperty("count","50");
                connection.setRequestProperty("page","1");
                connection.setRequestProperty("base_app","0");

                connection.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    Oauth2AccessToken mAccessToken;
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            //从bundle中解析token
            mAccessToken= Oauth2AccessToken.parseAccessToken(bundle);
            Log.i("AuthListener","worked");
            //从这里获取用户输入的电话号码信息
            String phoneNum=mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()){
                AccessTokenKeeper.writeAccessToken(getActivity(),mAccessToken);
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
        }
    }


}
