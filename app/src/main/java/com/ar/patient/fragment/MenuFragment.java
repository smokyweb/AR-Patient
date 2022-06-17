package com.ar.patient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.activity.ContactUsActivity;
import com.ar.patient.activity.FAQActivity;
import com.ar.patient.activity.LoginActivity;
import com.ar.patient.activity.NewsFeedActivity;
import com.ar.patient.activity.SettingsActivity;
import com.ar.patient.activity.SubscrotionActivity;
import com.ar.patient.activity.TermsAndConditionActivity;
import com.ar.patient.baseclass.BaseFragment;
import com.ar.patient.databinding.FragmentMenuBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.BaseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    private FragmentMenuBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);

        init();
        return binding.getRoot();
    }

    private void init() {
        binding.linNewsFeed.setOnClickListener(this);
        binding.linSettings.setOnClickListener(this);
        binding.linFAQ.setOnClickListener(this);
        binding.linAboutUs.setOnClickListener(this);
        binding.linPrivacy.setOnClickListener(this);
        binding.linTermsAndConditions.setOnClickListener(this);
        binding.linContactUs.setOnClickListener(this);
        binding.linlogout.setOnClickListener(this);
        binding.linsubscription.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.IS_MENU_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Config.IS_MENU_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Config.IS_MENU_DASHBOARD_REFERESH = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Config.IS_MENU_DASHBOARD_REFERESH) {
            Utils.loadImage(getActivity(), Pref.getValue(getActivity(), Config.PREF_AVATAR, ""), R.drawable.avtar, binding.ImgUser);
            binding.tvUserName.setText(Pref.getValue(getActivity(), Config.PREF_NAME, ""));
            Config.IS_MENU_DASHBOARD_REFERESH = true;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linsubscription:
                Intent news = new Intent(getActivity(), SubscrotionActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linNewsFeed:
                news = new Intent(getActivity(), NewsFeedActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linSettings:
                news = new Intent(getActivity(), SettingsActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linFAQ:
                news = new Intent(getActivity(), FAQActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linAboutUs:
                news = new Intent(getActivity(), TermsAndConditionActivity.class);
                news.putExtra("Code", Config.ABOUT_US);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linTermsAndConditions:
                news = new Intent(getActivity(), TermsAndConditionActivity.class);
                news.putExtra("Code", Config.TERMS_AND_CONDITION);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linPrivacy:
                news = new Intent(getActivity(), TermsAndConditionActivity.class);
                news.putExtra("Code", Config.PRIVCY_POLICY);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linContactUs:
                news = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(news);
                startAnimation();
                break;
            case R.id.linlogout:
                showDialog(getActivity(), "Are you sure you want to logout?", "YES", "No", new DialogClickListener() {
                    @Override
                    public void onClick() {
                        doLogout();
                    }
                });
                break;

        }
    }

    private void doLogout() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(getActivity(), Config.PREF_USERID, ""))
                .build();

        ARPatientApplication.getRestClient().getApiService().logout(requestBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    Pref.clearData(getActivity());
                    getActivity().finishAffinity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    startAnimation();
                }
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                hideProgress();
            }
        });
    }
}