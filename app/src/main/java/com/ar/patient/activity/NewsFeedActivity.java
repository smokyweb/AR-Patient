package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.adapter.NewsFeedListAdapter;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityNewsFeedBinding;
import com.ar.patient.responsemodel.NewsFeedResponse;
import com.ar.patient.responsemodel.Newsfeed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedActivity extends BaseActivity {

    private ActivityNewsFeedBinding binding;
    private NewsFeedListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_feed);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.recnewsfeed.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        adapter = new NewsFeedListAdapter(NewsFeedActivity.this, new NewsFeedListAdapter.Listner() {
            @Override
            public void onClick(Newsfeed value) {
                Intent details = new Intent(NewsFeedActivity.this, NewsFeedDetailsActivity.class);
                details.putExtra("name", value.getTitle());
                details.putExtra("url", value.getUrl());
                details.putExtra("isform", "newsfeed");
                startActivity(details);
                startAnimation();
            }
        });
        binding.recnewsfeed.setAdapter(adapter);


        newsfeed();
    }

    private void newsfeed() {
        if (!isOnline())
            return;
        ARPatientApplication.getRestClient().getApiService().newsfeed().enqueue(new Callback<NewsFeedResponse>() {
            @Override
            public void onResponse(Call<NewsFeedResponse> call, Response<NewsFeedResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        adapter.addAll(response.body().getNewsfeeds());
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsFeedResponse> call, Throwable t) {
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