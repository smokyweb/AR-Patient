package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityLoginBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.DrawableClickListener;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.LoginResponse;
import com.ar.patient.uc.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("token", "Fetching FCM registration token sucessed :: " + token);
                        Pref.setValue(LoginActivity.this, Config.PREF_FIREBASE_TOKEN, token);
                    }
                });

        InitViews();
    }

    private void InitViews() {
        binding.btnLogin.setOnClickListener(this);
        binding.tvNeedAccount.setOnClickListener(this);
        binding.linForgotPassword.setOnClickListener(this);
        binding.EdtPassword.setTag("false");
        binding.EdtPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.EdtPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.EdtPassword.getTag().equals("true")) {
                    binding.EdtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_white, 0);
                    binding.EdtPassword.setTag("false");
                    binding.EdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.EdtPassword.setSelection(binding.EdtPassword.length());
                } else {
                    binding.EdtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_off_white, 0);
                    binding.EdtPassword.setTag("true");
                    binding.EdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.EdtPassword.setSelection(binding.EdtPassword.length());
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(LoginActivity.this, getString(R.string.email_alert), null);
                } else if (!Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(LoginActivity.this, getString(R.string.email_alert), null);
                } else if (binding.EdtPassword.getText().toString().isEmpty()) {
                    showValidationDialog(LoginActivity.this, getString(R.string.password_blank_alert), null);
                } else if (binding.EdtPassword.getText().length() < 8) {
                    showValidationDialog(LoginActivity.this, getString(R.string.password_alert), null);
                } else {
                    callLoginAPI();
                }
                break;
            case R.id.tvNeedAccount:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                startAnimation();
                break;
            case R.id.linForgotPassword:
                intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
                startAnimation();
                break;
        }
    }

    private void callLoginAPI() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "" + binding.EdtEmail.getText().toString())
                .addFormDataPart("password", "" + binding.EdtPassword.getText().toString())
                .addFormDataPart("push_id", "" + Pref.getValue(LoginActivity.this, Config.PREF_FIREBASE_TOKEN, ""))
                .addFormDataPart("device_token", "" + "")
                .addFormDataPart("device_type", "" + "1")
                .build();

        ARPatientApplication.getRestClient().getApiService().Login(requestBody).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equalsIgnoreCase("0")) {
                            Pref.setValue(LoginActivity.this, Config.PREF_USERID, response.body().getUserId());
                            Pref.setValue(LoginActivity.this, Config.PREF_EMAIL, response.body().getEmail());
                            Pref.setValue(LoginActivity.this, Config.PREF_SESSION, response.body().getSessionId());
                            Pref.setValue(LoginActivity.this, Config.PREF_NAME, response.body().getName());
                            Pref.setValue(LoginActivity.this, Config.PREF_AVATAR, response.body().getAvatar());
                            Pref.setValue(LoginActivity.this, Config.PREF_ROLE, response.body().getRole());
                            Pref.setValue(LoginActivity.this, Config.PREF_IS_PUBLIC, response.body().getIsPublic());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            startAnimation();
                            finish();
                        } else {
                            showValidationDialog(LoginActivity.this, response.body().getMessage(), null);
                        }

                    } else {
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.d("token", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        // Get new FCM registration token
                                        String token = task.getResult();
                                        Log.d("token", "Fetching FCM registration token sucessed :: " + token);
                                        Pref.setValue(LoginActivity.this, Config.PREF_FIREBASE_TOKEN, token);
                                    }
                                });
                        showValidationDialog(LoginActivity.this, response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }
}