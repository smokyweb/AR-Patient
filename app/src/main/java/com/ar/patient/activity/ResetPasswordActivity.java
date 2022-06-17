package com.ar.patient.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityResetPasswordBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.DrawableClickListener;
import com.ar.patient.responsemodel.BaseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {

    ActivityResetPasswordBinding binding;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        email = getIntent().getStringExtra(Config.ARGEMAIL);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.edtConPassword.setTag("false");
        binding.edtConPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edtConPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.edtConPassword.getTag().equals("true")) {
                    binding.edtConPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_white, 0);
                    binding.edtConPassword.setTag("false");
                    binding.edtConPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edtConPassword.setSelection(binding.edtConPassword.length());
                } else {
                    binding.edtConPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_off_white, 0);
                    binding.edtConPassword.setTag("true");
                    binding.edtConPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edtConPassword.setSelection(binding.edtConPassword.length());
                }
                return false;
            }
        });

        binding.edtPassword.setTag("false");
        binding.edtPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edtPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.edtPassword.getTag().equals("true")) {
                    binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_white, 0);
                    binding.edtPassword.setTag("false");
                    binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edtPassword.setSelection(binding.edtPassword.length());
                } else {
                    binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.ic_visibility_off_white, 0);
                    binding.edtPassword.setTag("true");
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edtPassword.setSelection(binding.edtPassword.length());
                }
                return false;
            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtOtp.getText().length() < 6) {
                    showValidationDialog(ResetPasswordActivity.this, getString(R.string.otp_alert), null);
                } else if (binding.edtPassword.getText().length() < 8) {
                    showValidationDialog(ResetPasswordActivity.this, getString(R.string.password_alert), null);
                } else if (!binding.edtConPassword.getText().toString().equals(binding.edtPassword.getText().toString())) {
                    showValidationDialog(ResetPasswordActivity.this, getString(R.string.con_password_alert), null);
                } else {
                    callResetAPI();
                }
            }
        });

        binding.edtPassword.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (end == 1) {
                if (Character.isWhitespace(source.charAt(0))) {
                    return "";
                }
            }
            return null;
        }});
        binding.edtConPassword.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (end == 1) {
                if (Character.isWhitespace(source.charAt(0))) {
                    return "";
                }
            }
            return null;
        }});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAnimation();
    }

    private void callResetAPI() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", "" + email)
                .addFormDataPart("password", "" + binding.edtPassword.getText().toString())
                .addFormDataPart("otp", "" + binding.edtOtp.getText().toString())
                .build();

        ARPatientApplication.getRestClient().getApiService().resetPassword(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        showValidationDialog(ResetPasswordActivity.this, response.body().getMessage(), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                onBackPressed();
                            }
                        });

                    } else {
                        showValidationDialog(ResetPasswordActivity.this, response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                hideProgress();
            }
        });
    }

}