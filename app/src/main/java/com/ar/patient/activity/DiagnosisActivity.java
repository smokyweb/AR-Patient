package com.ar.patient.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityDiagnosisBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.DiagnosisResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagnosisActivity extends BaseActivity implements View.OnClickListener {

    String currentDateandTime;
    private ActivityDiagnosisBinding binding;
    private Integer ChatCount = 0;
    private String patientId = "";
    private String isfrom = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diagnosis);

        if (getIntent() != null) {
            ChatCount = getIntent().getIntExtra("chatsize", 0);
            patientId = getIntent().getStringExtra("patientId");
            isfrom = getIntent().getStringExtra("isfrom");

            Log.d("mytag", "ChatCount Value : " + "" + ChatCount);
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());
        Log.d("mytag", "Current time : " + currentDateandTime);
        initviews();
    }


    private void initviews() {
        binding.imgBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.imginfo.setOnClickListener(this);

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
                if (binding.Edtdiagnosis.getText().toString().isEmpty()) {
                    showValidationDialog(DiagnosisActivity.this, "Please enter diagnosis.", null);
                } else if (binding.Edtsubjectivenotes.getText().toString().isEmpty()) {
                    showValidationDialog(DiagnosisActivity.this, "Please enter Subjective notes.", null);
                } else if (binding.Edtobjectivenotes.getText().toString().isEmpty()) {
                    showValidationDialog(DiagnosisActivity.this, "Please enter Objective notes.", null);
                } else if (binding.Edtassessmentnotes.getText().toString().isEmpty()) {
                    showValidationDialog(DiagnosisActivity.this, "Please enter Assessment notes.", null);
                } else if (binding.Edtplannotes.getText().toString().isEmpty()) {
                    showValidationDialog(DiagnosisActivity.this, "Please enter Plan notes.", null);
                } else {
                    Diagnosis();
                }
                break;
            case R.id.imginfo:
                showfullscreendialog();
                break;
        }
    }

    public void showfullscreendialog() {
        Dialog dialog = new Dialog(this, R.style.full_screen_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.semi_trans);
        dialog.setContentView(R.layout.framr_help);
        dialog.show();

        ImageView close = dialog.findViewById(R.id.imgclose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void Diagnosis() {
        if (!isOnline())
            return;

        String testId = "";
        if (ChatCount == 0) {
            testId = "0";
        } else {
            testId = Pref.getValue(DiagnosisActivity.this, Config.PREF_TEST_ID, "");
        }


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("diagnosis", "" + binding.Edtdiagnosis.getText().toString())
                .addFormDataPart("subjective", "" + binding.Edtsubjectivenotes.getText().toString())
                .addFormDataPart("objective", "" + binding.Edtobjectivenotes.getText().toString())
                .addFormDataPart("assessment", "" + binding.Edtassessmentnotes.getText().toString())
                .addFormDataPart("plan", "" + binding.Edtplannotes.getText().toString())
                .addFormDataPart("user_id", "" + Pref.getValue(DiagnosisActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("patient_id", "" + patientId)
                .addFormDataPart("test_id", "" + testId)
                .addFormDataPart("sys_time", "" + currentDateandTime)
                .build();


        ARPatientApplication.getRestClient().getApiService().diagnosis(requestBody).enqueue(new Callback<DiagnosisResponse>() {
            @Override
            public void onResponse(Call<DiagnosisResponse> call, Response<DiagnosisResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        if (isfrom.equalsIgnoreCase("examsubmit")) {
                            Intent testresult = new Intent(DiagnosisActivity.this, TestResultActivity.class);
                            testresult.putExtra("id", response.body().getTestId());
                            testresult.putExtra("isfrom", "examsubmit");
                            startActivity(testresult);
                            startAnimation();
                            finish();
                        } else {
                            Pref.setValue(DiagnosisActivity.this, Config.PREF_TEST_ID, response.body().getTestId());
                            finish();
                            backAnimation();
                        }
                    } else {
                        showValidationDialog(DiagnosisActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(DiagnosisActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<DiagnosisResponse> call, Throwable t) {

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