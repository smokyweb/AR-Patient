package com.ar.patient;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.ar.patient.baseclass.Foreground;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.retrofit.RestClient;
import com.ar.patient.service.MyService;


public class ARPatientApplication extends Application {
    private static final String TAG = ARPatientApplication.class.getSimpleName();
    public static String userType;
    private static ARPatientApplication mInstance = null;

    // public static HomeVerticalModel.courseRes.courceList courceList;
    private static RestClient restClient;
    private Config mConfig;
    private SharedPreferences mPref;

    public static RestClient getRestClient() {
        return restClient;
    }

    public static synchronized ARPatientApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    //public static AgoraEngine mAgoraEngine;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        restClient = new RestClient(this);

        Foreground.init(this).addListener(new Foreground.Listener() {
            @Override
            public void onBecameForeground() {
                Log.d("mytag", "::::onBecameForeground::::");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Pref.getValue(ARPatientApplication.this, Config.PREF_RECORD_STATUS, false)) {
                            if (permissioncheck() && !Utils.isMyServiceRunning(ARPatientApplication.this, MyService.class)) {
                                startService(new Intent(ARPatientApplication.this, MyService.class));
                            }
                        }
                    }
                }, 1000);
//                stopService(new Intent(MyApp.this, MyService.class));
            }

            @Override
            public void onBecameBackground() {
                Log.d("mytag", "::::onBecameForeground::::");
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("mytag", "::::onBecameForeground::::");
                        if (Pref.getValue(ARPatientApplication.this, Config.PREF_RECORD_STATUS, false)) {
                            if (permissioncheck() && !Utils.isMyServiceRunning(ARPatientApplication.this, MyService.class)) {
                                startService(new Intent(ARPatientApplication.this, MyService.class));
                            }
                        }
                    }
                }, 1000);*/

            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // mAgoraEngine.release();
    }

    public Config config() {
        return mConfig;
    }

    public SharedPreferences preferences() {
        return mPref;
    }

    public boolean permissioncheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}