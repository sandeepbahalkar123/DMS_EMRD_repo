package com.rescribe.doctor.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.doctor.R;
import com.rescribe.doctor.util.RescribeConstants;

public class ConnectSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_splash);
        doNext();
    }

    private void doNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConnectSplashActivity.this, PatientConnectActivity.class);
                startActivity(intent);
                finish();
            }
        }, RescribeConstants.TIME_STAMPS.TWO_SECONDS);
    }
}
