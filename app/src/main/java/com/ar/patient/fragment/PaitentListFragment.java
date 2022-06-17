package com.ar.patient.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.activity.DownloadingPatientModelActivity;
import com.ar.patient.activity.PatientExamActivity;
import com.ar.patient.activity.SubscrotionActivity;
import com.ar.patient.adapter.PatientListAdapter;
import com.ar.patient.baseclass.BaseFragment;
import com.ar.patient.databinding.FragmentPaitentListBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.patientsresponse.PatientsListResponse;
import com.ar.patient.responsemodel.patientsresponse.Type;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaitentListFragment extends BaseFragment {

    private FragmentPaitentListBinding binding;
    private PatientListAdapter adapter;
    private ArrayList<Type> list = new ArrayList<>();
    private ArrayList<PatientsListResponse.BodyParts> bodyPartsArrayList;
    public PatientsListResponse responsebean;
    public Type beantype;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_paitent_list, container, false);

        init();
        return binding.getRoot();
    }

    private void init() {
        binding.recpagelist.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PatientListAdapter(getActivity(), new PatientListAdapter.Listner() {
            @Override
            public void onClick(Type bean) {
                beantype = bean;
                if (Pref.getValue(getActivity(), Config.PREF_KEY_IS_SUBSCRIPTION, "").equalsIgnoreCase("1") &&
                        !Pref.getValue(getActivity(), Config.PREF_KEY_IS_SUBSCRIPTION, "").isEmpty()) {
                    if (Pref.getValue(getActivity(), Config.PREF_KEY_SUBSCRIPTION_BUNDLE, "").equalsIgnoreCase(Config.SKU_MONTLY_SILVER) &&
                            (bean.getUsertype().equalsIgnoreCase("1") || bean.getUsertype().equalsIgnoreCase("0"))) {
                        checkfile(bean);
                    } else if (Pref.getValue(getActivity(), Config.PREF_KEY_SUBSCRIPTION_BUNDLE, "").equalsIgnoreCase(Config.SKU_MONTLY_GOLD) &&
                            (bean.getUsertype().equalsIgnoreCase("0") || bean.getUsertype().equalsIgnoreCase("1") || bean.getUsertype().equalsIgnoreCase("2"))) {
                        checkfile(bean);
                    } else {
                        showDialog(getActivity(), "Sorry,you can't access the patient as that patient will be available under "
                                + (bean.getUsertype().equalsIgnoreCase("1") ? "Silver Plan." : "Gold Plan.") + "Please subscribe for that plan.", "Subscribe", "Cancel", new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Intent news = new Intent(getActivity(), SubscrotionActivity.class);
                                startActivityForResult(news, 222);
                                startAnimation();
                                Config.IS_PATIENT_DASHBOARD_REFERESH = false;
                            }
                        });
                    }
                } else {
                    if (bean.getUsertype().equalsIgnoreCase("0")) {
                        checkfile(bean);
                    } else {
                        showDialog(getActivity(), "Sorry,you can't access the patient as that patient will be available under "
                                + (bean.getUsertype().equalsIgnoreCase("1") ? "Silver Plan." : "Gold Plan.") + "Please subscribe for that plan.", "Subscribe", "Cancel", new DialogClickListener() {
                            @Override
                            public void onClick() {
                                Intent news = new Intent(getActivity(), SubscrotionActivity.class);
                                startActivityForResult(news, 222);
                                startAnimation();
                                Config.IS_PATIENT_DASHBOARD_REFERESH = false;
                            }
                        });
                    }

                }

            }
        });


        binding.recpagelist.setAdapter(adapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    filter(s.toString());
                } else {
                    adapter.updateList(list);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Config.IS_PATIENT_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Config.IS_PATIENT_DASHBOARD_REFERESH = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Config.IS_PATIENT_DASHBOARD_REFERESH = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Config.IS_PATIENT_DASHBOARD_REFERESH) {
            if (!isOnline())
                return;
            getPatientList();
            Config.IS_PATIENT_DASHBOARD_REFERESH = true;
        }
    }

    private void checkfile(Type bean) {
        File myDir = new File(getActivity().getExternalCacheDir() + Config.MAINFOLDERNAME);
        if (myDir.exists()) {
            File[] files = myDir.listFiles();
            if (files.length != 0) {
                Log.d("Files", "Size: " + files.length);
                boolean checkFile = false;
                for (File file : files) {
                    Log.d("Files", "FileName:" + file.getName());
                    if (file.getName().equalsIgnoreCase(Utils.getFileNameFromURL(Config.MEDIA_URL + "/" + bean.getFrontimage()))) {
                        Log.d("mytag", "Front Image Paths : " + file.getAbsolutePath());
                    } else if (file.getName().equalsIgnoreCase(Utils.getFileNameFromURL(Config.MEDIA_URL + "/" + bean.getBackimage()))) {
                        checkFile = true;
                        Log.d("mytag", "Back Image Paths : " + file.getAbsolutePath());
                        break;
                    }
                }

                if (checkFile) {
                    Intent exam = new Intent(getActivity(), PatientExamActivity.class);
                    exam.putExtra("Type", bean);
                    exam.putExtra("BodyPartsArrayList", bodyPartsArrayList);
                    getActivity().startActivity(exam);
                    startAnimation();
                    Config.IS_PATIENT_DASHBOARD_REFERESH = false;
                } else {
                    Intent exam = new Intent(getActivity(), DownloadingPatientModelActivity.class);
                    exam.putExtra("front_image", Config.MEDIA_URL + "/" + bean.getFrontimage());
                    exam.putExtra("back_image", Config.MEDIA_URL + "/" + bean.getBackimage());
                    exam.putExtra("Type", bean);
                    exam.putExtra("BodyPartsArrayList", bodyPartsArrayList);
                    getActivity().startActivity(exam);
                    startAnimation();
                    Config.IS_PATIENT_DASHBOARD_REFERESH = false;
                }
            } else {
                Intent exam = new Intent(getActivity(), DownloadingPatientModelActivity.class);
                exam.putExtra("front_image", Config.MEDIA_URL + "/" + bean.getFrontimage());
                exam.putExtra("back_image", Config.MEDIA_URL + "/" + bean.getBackimage());
                exam.putExtra("Type", bean);
                exam.putExtra("BodyPartsArrayList", bodyPartsArrayList);
                getActivity().startActivity(exam);
                startAnimation();
                Config.IS_PATIENT_DASHBOARD_REFERESH = false;
            }
        } else {
            Intent exam = new Intent(getActivity(), DownloadingPatientModelActivity.class);
            exam.putExtra("front_image", Config.MEDIA_URL + "/" + bean.getFrontimage());
            exam.putExtra("back_image", Config.MEDIA_URL + "/" + bean.getBackimage());
            exam.putExtra("Type", bean);
            exam.putExtra("BodyPartsArrayList", bodyPartsArrayList);
            getActivity().startActivity(exam);
            startAnimation();
            Config.IS_PATIENT_DASHBOARD_REFERESH = false;
        }
    }

    void filter(String text) {
        ArrayList<Type> temp = new ArrayList();
        for (Type d : list) {
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }
    private void getPatientList() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(getActivity(), Config.PREF_USERID, ""))
                .build();

        ARPatientApplication.getRestClient().getApiService().getpatientslist(requestBody).enqueue(new Callback<PatientsListResponse>() {
            @Override
            public void onResponse(Call<PatientsListResponse> call, Response<PatientsListResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equalsIgnoreCase("0")) {
                            responsebean = response.body();
                            list = response.body().getTypes();
                            bodyPartsArrayList = response.body().getBodyPartsArrayList();
                            adapter.addAll(response.body().getTypes());

                            Pref.setValue(getActivity(), Config.PREF_KEY_IS_SUBSCRIPTION, response.body().getIssubscribed());
                            Pref.setValue(getActivity(), Config.PREF_KEY_SUBSCRIPTION_EX_DATE, response.body().getExpDate());
                            Pref.setValue(getActivity(), Config.PREF_KEY_SUBSCRIPTION_TOKEN, response.body().getSubscription_token());
                            Pref.setValue(getActivity(), Config.PREF_KEY_SUBSCRIPTION_BUNDLE,
                                    response.body().getSubscriptionBundleId().equalsIgnoreCase(Config.SKU_MONTLY_SILVER) ? Config.SKU_MONTLY_SILVER : Config.SKU_MONTLY_GOLD);

                            String deliveryoption = Utils.featuredlist(response.body().getSubscriptionfeaturedlist());
                            Pref.setValue(getActivity(), Config.FEATURE_LIST, deliveryoption);


                        } else {
                            showValidationDialog(getActivity(), response.body().getMessage(), null);
                        }

                    } else {
                        showValidationDialog(getActivity(), response.body().getMessage(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientsListResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 222) {
            checkfile(beantype);
        }
    }
}