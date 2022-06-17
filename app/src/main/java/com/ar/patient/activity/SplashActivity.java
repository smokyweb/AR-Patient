package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Pref.getValue(SplashActivity.this, Config.PREF_USERID, "").equalsIgnoreCase("")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    startAnimation();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    startAnimation();
                }
            }
        }, 3000);
    }
}