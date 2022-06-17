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
import com.ar.patient.databinding.ActivitySoapNotesBinding;
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

public class SOAPNotesActivity extends BaseActivity {

    private ActivitySoapNotesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_soap_notes);

        binding.imgBack.setOnClickListener(v -> onBackPressed());
        if (getIntent() != null) {
            binding.txtsubjectivenotes.setText(getIntent().getStringExtra("subjective"));
            binding.txtobjectivenotes.setText(getIntent().getStringExtra("objective"));
            binding.txtassessmentnotes.setText(getIntent().getStringExtra("assesment"));
            binding.txtplannotes.setText(getIntent().getStringExtra("plannotes"));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
    }
}