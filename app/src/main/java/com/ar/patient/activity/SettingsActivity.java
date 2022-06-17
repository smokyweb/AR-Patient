package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivitySettingsBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.BaseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        initviews();
    }

    private void initviews() {
        binding.linEditProfile.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.lindeleteAccount.setOnClickListener(this);
        binding.linChangePassword.setOnClickListener(this);
        binding.linlogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                backAnimation();
                break;
            case R.id.linEditProfile:
                Intent news = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(news);
                startAnimation();
                break;

            case R.id.linChangePassword:
                news = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.lindeleteAccount:
                showDialog(SettingsActivity.this, "Are you Sure you want to delete Account!", new DialogClickListener() {
                    @Override
                    public void onClick() {
                        deleteaccount();
                    }
                });
                break;
            case R.id.linlogout:
                showDialog(SettingsActivity.this, "Are you Sure you want to logout!", new DialogClickListener() {
                    @Override
                    public void onClick() {
                        doLogout();
                    }
                });
                break;
        }
    }

    private void deleteaccount() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(SettingsActivity.this, Config.PREF_USERID, ""))
                .build();

        ARPatientApplication.getRestClient().getApiService().deleteAccount(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    Pref.clearData(SettingsActivity.this);
                    finishAffinity();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    startAnimation();
                }
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void doLogout() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(SettingsActivity.this, Config.PREF_USERID, ""))
                .build();

        ARPatientApplication.getRestClient().getApiService().logout(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    Pref.clearData(SettingsActivity.this);
                    finishAffinity();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    startAnimation();
                }
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                hideProgress();
            }
        });
    }
}