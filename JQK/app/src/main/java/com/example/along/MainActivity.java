package com.example.along;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.along.jqk.R;

public class MainActivity extends Activity {
    LinearLayout contentPanel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clone_weibo_home_layout);
        contentPanel = (LinearLayout) findViewById(R.id.content_panel);
        Fragment contentFragment = new WeiBoHomeFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_panel, contentFragment);
        transaction.commit();
    }

}