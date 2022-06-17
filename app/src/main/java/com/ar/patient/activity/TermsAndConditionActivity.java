package com.ar.patient.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityTermsAndConditionBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.responsemodel.CmsPageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionActivity extends BaseActivity {

    private ActivityTermsAndConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().getStringExtra("Code").equalsIgnoreCase(Config.ABOUT_US)) {
            binding.tvforgotPassword.setText(getString(R.string.about_us));
        } else if (getIntent().getStringExtra("Code").equalsIgnoreCase(Config.TERMS_AND_CONDITION)) {
            binding.tvforgotPassword.setText(getString(R.string.terms_amp_conditions));
        } else if (getIntent().getStringExtra("Code").equalsIgnoreCase(Config.PRIVCY_POLICY)) {
            binding.tvforgotPassword.setText(getString(R.string.privacy_policy));
        }


        TermsandCondition();
    }

    private void TermsandCondition() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cms_id", "" + getIntent().getStringExtra("Code"))
                .build();

        ARPatientApplication.getRestClient().getApiService().terms(requestBody).enqueue(new Callback<CmsPageResponse>() {
            @Override
            public void onResponse(Call<CmsPageResponse> call, Response<CmsPageResponse> response) {
                if (response.isSuccessful()) {
                    binding.webView.getSettings().setJavaScriptEnabled(true);
                    binding.webView.getSettings().setBuiltInZoomControls(false);
                    binding.webView.loadUrl(response.body().getUrl());
                    binding.webView.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }

                        public void onPageFinished(WebView view, String url) {
                            hideProgress();
                        }

                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            hideProgress();
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<CmsPageResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAnimation();
    }
}