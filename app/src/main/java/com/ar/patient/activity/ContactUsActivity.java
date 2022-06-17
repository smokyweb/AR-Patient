package com.ar.patient.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityContactUsBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.BaseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity implements View.OnClickListener {

    private ActivityContactUsBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);

        initviews();
    }


    private void initviews() {
        binding.imgBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        /*Typeface type = Typeface.createFromAsset(getAssets(),"fonts/poppins_regular.ttf");
        binding.EdtPhone.setTypeface(type);*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                backAnimation();
                break;
            case R.id.btnSubmit:
                if (binding.EdtFullname.getText().toString().isEmpty()) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.fullname_blank_alert), null);
                } else if (binding.EdtEmail.getText().toString().isEmpty()) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.email_blank_alert), null);
                } else if (!com.ar.patient.uc.Utils.isValidEmailId(binding.EdtEmail.getText().toString())) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.email_alert), null);
                } else if (binding.EdtPhone.getText().toString().isEmpty()) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.phone_alert), null);
                } else if (binding.EdtPhone.getText().toString().length() < 10) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.phone_alert_lenght), null);
                } else if (binding.Edtmessege.getText().toString().isEmpty()) {
                    showValidationDialog(ContactUsActivity.this, getString(R.string.msg_alert), null);
                } else {
                    contactUs();
                }
                break;
        }
    }

    public void contactUs() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(ContactUsActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("name", "" + binding.EdtFullname.getText().toString())
                .addFormDataPart("email", "" + binding.EdtEmail.getText().toString())
                .addFormDataPart("phone", "" + binding.EdtPhone.getUnMaskedText())
                .addFormDataPart("message", "" + binding.Edtmessege.getText().toString())
                .build();


        ARPatientApplication.getRestClient().getApiService().contactus(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        showSingleButtonDialog(ContactUsActivity.this, getString(R.string.sucess_contact_us),
                                new DialogClickListener() {
                                    @Override
                                    public void onClick() {
                                        binding.EdtFullname.setText("");
                                        binding.EdtEmail.setText("");
                                        binding.EdtPhone.setText("");
                                        binding.Edtmessege.setText("");
                                        binding.EdtFullname.requestFocus();
                                    }
                                });
                    } else {
                        showValidationDialog(ContactUsActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(ContactUsActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }
}