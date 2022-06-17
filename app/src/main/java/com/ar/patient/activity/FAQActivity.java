package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.adapter.FAQListAdapter;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityFaqBinding;
import com.ar.patient.responsemodel.Faq;
import com.ar.patient.responsemodel.FaqsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends BaseActivity {

    private ActivityFaqBinding binding;
    private FAQListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);

        binding.tvforgotPassword.setText("F.A.Q");
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.recnewsfeed.setLayoutManager(new LinearLayoutManager(FAQActivity.this));
        adapter = new FAQListAdapter(FAQActivity.this, new FAQListAdapter.Listner() {
            @Override
            public void onClick(Faq value) {
                Intent details = new Intent(FAQActivity.this, NewsFeedDetailsActivity.class);
                details.putExtra("name", value.getTitle());
                details.putExtra("url", value.getUrl());
                details.putExtra("isform", "faq");
                startActivity(details);
                startAnimation();
            }
        });
        binding.recnewsfeed.setAdapter(adapter);


        faqlist();
    }

    private void faqlist() {
        if (!isOnline())
            return;
        ARPatientApplication.getRestClient().getApiService().faqlist().enqueue(new Callback<FaqsResponse>() {
            @Override
            public void onResponse(Call<FaqsResponse> call, Response<FaqsResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        adapter.addAll(response.body().getFaqs());
                    }
                }
            }

            @Override
            public void onFailure(Call<FaqsResponse> call, Throwable t) {
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