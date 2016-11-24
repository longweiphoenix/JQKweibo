package com.example.along;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.along.jqk.R;

/**
 * Created by Long on 2016/11/22.
 */
public class MyActivity extends Activity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);
        button= (Button) findViewById(R.id.my_bn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
