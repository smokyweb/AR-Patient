package com.ar.patient.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityTermsAndConditionBinding;

public class NewsFeedDetailsActivity extends BaseActivity {

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

        if (getIntent() != null) {

            if (getIntent().getStringExtra("isform").equalsIgnoreCase("newsfeed")) {
                binding.tvforgotPassword.setText("News Feed");
            } else {
                binding.tvforgotPassword.setText("F.A.Q");
            }

            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setBuiltInZoomControls(false);
            binding.webView.loadUrl(getIntent().getStringExtra("url"));
            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showProgress();
                }

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
    public void onBackPressed() {
        super.onBackPressed();
        backAnimation();
    }
}