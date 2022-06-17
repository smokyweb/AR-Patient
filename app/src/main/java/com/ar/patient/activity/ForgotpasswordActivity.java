package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityForgotPasswordBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.responsemodel.ForgotpasswordResponse;
import com.ar.patient.uc.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotpasswordActivity extends BaseActivity implements View.OnClickListener {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        InitViews();
    }

    private void InitViews() {
        binding.btnsubmit.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                backAnimation();
                break;
            case R.id.btnsubmit:
                if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(ForgotpasswordActivity.this, getString(R.string.email_blank_alert), null);
                } else if (!Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(ForgotpasswordActivity.this, getString(R.string.email_alert), null);
                } else {
                    callForgotAPI();
                }
                break;

        }
    }

    private void callForgotAPI() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", "" + binding.EdtEmail.getText().toString())
                .build();

        ARPatientApplication.getRestClient().getApiService().forgotPassword(requestBody).enqueue(new Callback<ForgotpasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotpasswordResponse> call, Response<ForgotpasswordResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body().getCode().equalsIgnoreCase("0")) {
                            showValidationDialog(ForgotpasswordActivity.this, response.body().getMessage(), new DialogClickListener() {
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(ForgotpasswordActivity.this, ResetPasswordActivity.class);
                                    intent.putExtra(Config.ARGEMAIL, binding.EdtEmail.getText().toString());
                                    startActivity(intent);
                                    finish();
                                    startAnimation();
                                }
                            });
                        } else {
                            showValidationDialog(ForgotpasswordActivity.this, response.body().getMessage(), null);
                        }
                    } else {
                        showValidationDialog(ForgotpasswordActivity.this, response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotpasswordResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }
}