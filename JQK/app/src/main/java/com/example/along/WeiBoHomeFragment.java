package com.example.along;

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

import com.example.along.jqk.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class WeiBoHomeFragment extends Fragment implements View.OnClickListener{
    ImageView radar;
    TextView friend_focus,userName,radarPop,scan,taxi;
    ListView listView;
    LayoutInflater layoutInflater;
    Oauth2AccessToken accessToken;
    List<WeiBoHome> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater=inflater;
        View view=inflater.inflate(R.layout.wei_bo_home_fragment,null);
        radar= (ImageView) view.findViewById(R.id.radar);
        friend_focus= (TextView) view.findViewById(R.id.friend_focus);

        //添加微博内容
        listView= (ListView) view.findViewById(R.id.list_weibo);
        accessToken=AccessTokenKeeper.readAccessToken(getActivity());
        WeiboAsyncTask weiboAsyncTask=new WeiboAsyncTask();
        weiboAsyncTask.execute(accessToken.getToken());
        getJSON();
        listView.setAdapter(new MyAdapter(list,getActivity()));
        userName= (TextView) view.findViewById(R.id.user_name);

        return view;
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
    StringBuilder stringBuilder;
    //异步线程获取数据
    public class WeiboAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            stringBuilder=new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                Oauth2AccessToken oauth2AccessToken=AccessTokenKeeper.readAccessToken(getActivity());
                connection.setRequestProperty("access_token",oauth2AccessToken.getToken());
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String s="";
                while((s=bufferedReader.readLine())!=null){
                    stringBuilder.append(s);
                }
                bufferedReader.close();
                inputStream.close();
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
    /*Oauth2AccessToken mAccessToken;*/
    /*class AuthListener implements WeiboAuthListener {

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
    }*/
    public void getJSON(){
        if(stringBuilder!=null){
            try {
                list=new ArrayList<WeiBoHome>();
                WeiBoHome weiBoHome=new WeiBoHome();
                JSONObject jsonObject=new JSONObject(stringBuilder.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("statuses");
                JSONObject object=jsonArray.getJSONObject(0);
                weiBoHome.commentNums=object.getInt("reposts_count");
                weiBoHome.content=object.getString("text");
                list.add(weiBoHome);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
