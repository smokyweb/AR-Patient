package com.ar.patient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.adapter.ExamListAdapter;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityMyProfileBinding;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.myprofile.MyProfileResponse;
import com.ar.patient.responsemodel.myprofile.Test;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends BaseActivity {

    public int mCurrentPageno = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private ActivityMyProfileBinding binding;
    private boolean loading = true; // True if we are still waiting for the last set of courseBeen to load.
    private LinearLayoutManager linearLayoutManager;
    private int totalrecordSize = 0;
    private ExamListAdapter examListadapter;
    private MyProfileResponse myProfileResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);

        initviews();
    }

    private void initviews() {
        linearLayoutManager = new LinearLayoutManager(MyProfileActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.recexamlist.setLayoutManager(linearLayoutManager);

        binding.tvforgotPassword.setText("Profile");

        binding.ImgEdit.setVisibility(View.GONE);
        binding.imgBack.setVisibility(View.VISIBLE);

        examListadapter = new ExamListAdapter(MyProfileActivity.this, new ExamListAdapter.Listner() {
            @Override
            public void onClick(Test value) {
                Intent testresult = new Intent(MyProfileActivity.this, TestResultActivity.class);
                testresult.putExtra("id", value.getTestId());
                testresult.putExtra("isfrom", "profile");
                startActivity(testresult);
            }
        });
        binding.recexamlist.setAdapter(examListadapter);

        binding.recexamlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalrecordSize > 50) {
                        totalrecordSize = 0;
                        loading = false;
                        mCurrentPageno++;
                        getMyProfile();
                    }
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                backAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyProfile();
    }

    private void getMyProfile() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + getIntent().getStringExtra("id"))
                .addFormDataPart("page_no", "" + mCurrentPageno)
                .build();

        ARPatientApplication.getRestClient().getApiService().getmyprofile(requestBody).enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        myProfileResponse = response.body();
                        binding.txtUserName.setText(response.body().getName());
                        binding.txtprecentages.setText(response.body().getResult() + "%");
                        binding.txtRank.setText(response.body().getRank());

                        Utils.loadImage(MyProfileActivity.this, response.body().getAvatar(), R.drawable.avtar, binding.ImgUser);

                        if (response.body().getTests().size() > 0) {
                            examListadapter.addAll(response.body().getTests());
                            loading = true;
                        } else {
                            loading = false;
                        }
                    } else {
                        showValidationDialog(MyProfileActivity.this, response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

}