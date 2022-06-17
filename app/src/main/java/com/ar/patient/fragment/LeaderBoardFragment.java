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
import com.ar.patient.activity.MyProfileActivity;
import com.ar.patient.adapter.LeaderboardListAdapter;
import com.ar.patient.baseclass.BaseFragment;
import com.ar.patient.databinding.FragmentLeaderboardBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.leaderboardresponse.Leaderboardresponse;
import com.ar.patient.responsemodel.leaderboardresponse.Student;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoardFragment extends BaseFragment {

    public int mCurrentPageno = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private FragmentLeaderboardBinding binding;
    private LeaderboardListAdapter adapter;
    private boolean loading = true; // True if we are still waiting for the last set of courseBeen to load.
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaderboard, container, false);

        init();
        return binding.getRoot();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recpagelist.setLayoutManager(linearLayoutManager);

        adapter = new LeaderboardListAdapter(getActivity(), new LeaderboardListAdapter.Listner() {
            @Override
            public void onClick(Student value) {
                Intent profile = new Intent(getActivity(), MyProfileActivity.class);
                profile.putExtra("id", value.getUserId());
                startActivity(profile);
                startAnimation();
            }
        });
        binding.recpagelist.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        binding.recpagelist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    getLeaderBoardlist();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.IS_LEADERBOARD_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Config.IS_LEADERBOARD_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Config.IS_LEADERBOARD_DASHBOARD_REFERESH = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Config.IS_LEADERBOARD_DASHBOARD_REFERESH) {
            if (!isOnline())
                return;
            getLeaderBoardlist();
            Config.IS_LEADERBOARD_DASHBOARD_REFERESH = true;
        }
    }



    private void getLeaderBoardlist() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(getActivity(), Config.PREF_USERID, ""))
                .addFormDataPart("page_no", "" + mCurrentPageno)
                .build();

        ARPatientApplication.getRestClient().getApiService().getLeaderboardlist(requestBody).enqueue(new Callback<Leaderboardresponse>() {
            @Override
            public void onResponse(Call<Leaderboardresponse> call, Response<Leaderboardresponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equalsIgnoreCase("0")) {
                            if (response.body().getStudents().size() != 0) {
                                adapter.addAll(response.body().getStudents());
                                loading = true;
                            } else {
                                loading = false;
                            }

                        } else {
                            showValidationDialog(getActivity(), response.body().getMessage(), null);
                        }

                    } else {
                        showValidationDialog(getActivity(), response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Leaderboardresponse> call, Throwable t) {
                hideProgress();
                loading = false;
            }
        });
    }


}