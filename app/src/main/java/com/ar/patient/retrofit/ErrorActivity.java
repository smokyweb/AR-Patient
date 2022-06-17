package com.ar.patient.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ar.patient.activity.LoginActivity;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.helper.Pref;


public class ErrorActivity extends BaseActivity {

    public static final String ARG_MESSAGE = "ARG_MESSAGE";
    public static final String ARG_IS_LOGIN = "ARG_IS_LOGIN";
    public static Activity activity;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;


        showSingleButtonDialog(activity, getIntent().getStringExtra(ARG_MESSAGE), new BaseActivity.DialogClickListener() {
            @Override
            public void onClick() {
                try {
                    Pref.clearData(ErrorActivity.this);
                    Intent iLogin = new Intent(ErrorActivity.this, LoginActivity.class);
                    iLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    iLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    iLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(iLogin);
                    finish();
                    startAnimation();
                } catch (Exception e) {
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        activity = null;
        super.onDestroy();
    }
}
