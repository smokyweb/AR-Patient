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
import com.ar.patient.databinding.ActivityChangePasswordBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.DrawableClickListener;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.BaseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.edtPassword.setTag("false");
        binding.edtPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edtPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.edtPassword.getTag().equals("true")) {
                    binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black, 0);
                    binding.edtPassword.setTag("false");
                    binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edtPassword.setSelection(binding.edtPassword.length());
                } else {
                    binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black, 0);
                    binding.edtPassword.setTag("true");
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edtPassword.setSelection(binding.edtPassword.length());
                }
                return false;
            }
        });

        binding.edtNewPassword.setTag("false");
        binding.edtNewPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edtNewPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.edtNewPassword.getTag().equals("true")) {
                    binding.edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black, 0);
                    binding.edtNewPassword.setTag("false");
                    binding.edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edtNewPassword.setSelection(binding.edtNewPassword.length());
                } else {
                    binding.edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black, 0);
                    binding.edtNewPassword.setTag("true");
                    binding.edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edtNewPassword.setSelection(binding.edtNewPassword.length());
                }
                return false;
            }
        });

        binding.edtConPassword.setTag("false");
        binding.edtConPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edtConPassword) {
            @Override
            public boolean onDrawableClick() {
                if (binding.edtConPassword.getTag().equals("true")) {
                    binding.edtConPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black, 0);
                    binding.edtConPassword.setTag("false");
                    binding.edtConPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edtConPassword.setSelection(binding.edtConPassword.length());
                } else {
                    binding.edtConPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black, 0);
                    binding.edtConPassword.setTag("true");
                    binding.edtConPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edtConPassword.setSelection(binding.edtConPassword.length());
                }
                return false;
            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtPassword.getText().toString().isEmpty()) {
                    showValidationDialog(ChangePasswordActivity.this, getString(R.string.password_blank_alert), null);
                } else if (binding.edtPassword.getText().length() < 8) {
                    showValidationDialog(ChangePasswordActivity.this, getString(R.string.password_alert), null);
                } else if (binding.edtNewPassword.getText().toString().isEmpty()) {
                    showValidationDialog(ChangePasswordActivity.this, "Please enter new Password", null);
                } else if (binding.edtNewPassword.getText().length() < 8) {
                    showValidationDialog(ChangePasswordActivity.this, getString(R.string.password_alert), null);
                } else if (binding.edtConPassword.getText().toString().isEmpty()) {
                    showValidationDialog(ChangePasswordActivity.this, "Please enter Confirm Password", null);
                } else if (binding.edtConPassword.getText().length() < 8) {
                    showValidationDialog(ChangePasswordActivity.this, getString(R.string.password_alert), null);
                } else if (!binding.edtConPassword.getText().toString().equals(binding.edtNewPassword.getText().toString())) {
                    showValidationDialog(ChangePasswordActivity.this, getString(R.string.con_password_alert), null);
                } else {
                    callChangeAPI();
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
        binding.edtNewPassword.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
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
        finish();
        backAnimation();
    }

    private void callChangeAPI() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(ChangePasswordActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("old_password", "" + binding.edtPassword.getText().toString())
                .addFormDataPart("new_password", "" + binding.edtNewPassword.getText().toString())
                .build();

        ARPatientApplication.getRestClient().getApiService().changePassword(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        showValidationDialog(ChangePasswordActivity.this, response.body().getMessage(), new DialogClickListener() {
                            @Override
                            public void onClick() {
                                onBackPressed();
                            }
                        });

                    } else {
                        showValidationDialog(ChangePasswordActivity.this, response.body().getMessage(), null);
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