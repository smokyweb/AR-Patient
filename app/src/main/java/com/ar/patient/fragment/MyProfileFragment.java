package com.ar.patient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.activity.EditProfileActivity;
import com.ar.patient.activity.TestResultActivity;
import com.ar.patient.adapter.ExamListAdapter;
import com.ar.patient.baseclass.BaseFragment;
import com.ar.patient.databinding.ActivityMyProfileBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.myprofile.MyProfileResponse;
import com.ar.patient.responsemodel.myprofile.Test;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileFragment extends BaseFragment {

    public int mCurrentPageno = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private ActivityMyProfileBinding binding;
    private boolean loading = true; // True if we are still waiting for the last set of courseBeen to load.
    private LinearLayoutManager linearLayoutManager;
    private int totalrecordSize = 0;
    private ExamListAdapter examListadapter;
    private MyProfileResponse myProfileResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_my_profile, container, false);

        initviews();
        return binding.getRoot();
    }


    private void initviews() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recexamlist.setLayoutManager(linearLayoutManager);

        binding.ImgEdit.setVisibility(View.VISIBLE);
        binding.imgBack.setVisibility(View.GONE);

        examListadapter = new ExamListAdapter(getActivity(), new ExamListAdapter.Listner() {
            @Override
            public void onClick(Test value) {
                Intent testresult = new Intent(getActivity(), TestResultActivity.class);
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
                    loading = false;
                    mCurrentPageno++;
                    getMyProfile();
                }
            }
        });

        binding.ImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent news = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(news);
                startAnimation();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.IS_MYPROFILE_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Config.IS_MYPROFILE_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Config.IS_MYPROFILE_DASHBOARD_REFERESH = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Config.IS_MYPROFILE_DASHBOARD_REFERESH) {
            if (!isOnline())
                return;
            getMyProfile();
            Config.IS_MYPROFILE_DASHBOARD_REFERESH = true;
        }
    }

    private void getMyProfile() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(getActivity(), Config.PREF_USERID, ""))
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

                        Utils.loadImage(getActivity(), response.body().getAvatar(), R.drawable.avtar, binding.ImgUser);

                        if (response.body().getTests().size() > 0) {
                            examListadapter.addAll(response.body().getTests());
                            loading = true;
                        } else {
                            loading = false;
                        }
                    } else {
                        showValidationDialog(getActivity(), response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                hideProgress();
                loading = false;
            }
        });
    }

}