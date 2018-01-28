package com.example.ama.homechef;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

/**
*
*/ public void openCamera(View v){
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    startActivity(intent);

    }

}
