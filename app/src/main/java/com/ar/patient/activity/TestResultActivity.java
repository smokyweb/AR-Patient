package com.ar.patient.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityTestResultBinding;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.responsemodel.testresultresponse.TestResultResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultActivity extends BaseActivity {

    private ActivityTestResultBinding binding;

    private String isfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_result);

        if (getIntent() != null) {
            isfrom = getIntent().getStringExtra("isfrom");
        }

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        testresultdetails();
    }


    private void testresultdetails() {

        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("test_id", "" + getIntent().getStringExtra("id"))
                .addFormDataPart("user_id", "" + Pref.getValue(TestResultActivity.this, Config.PREF_USERID, ""))
                .build();

        ARPatientApplication.getRestClient().getApiService().testdetails(requestBody).enqueue(new Callback<TestResultResponse>() {
            @Override
            public void onResponse(Call<TestResultResponse> call, Response<TestResultResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {

                    binding.llQuestionsAsked.removeAllViews();
                    if (response.body().getAskedQue().size() > 0) {
                        binding.txtNoDataQuestionAsk.setVisibility(View.GONE);
                        binding.llQuestionsAsked.setVisibility(View.VISIBLE);
                        for (TestResultResponse.ResultBodyPart resultBodyPart : response.body().getAskedQue()) {
                            View view = LayoutInflater.from(TestResultActivity.this).inflate(R.layout.row_result_body_part, null);
                            ((TextView) view.findViewById(R.id.txtName)).setText(resultBodyPart.getName());
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageResource(R.drawable.circle_green);
                            binding.llQuestionsAsked.addView(view);
                        }
                    } else {
                        binding.txtNoDataQuestionAsk.setVisibility(View.VISIBLE);
                        binding.llQuestionsAsked.setVisibility(View.GONE);
                    }

                    binding.llQuestionsNotAsked.removeAllViews();
                    if (response.body().getNotAskedQue().size() > 0) {
                        binding.txtNoDataQuestionNotAsk.setVisibility(View.GONE);
                        binding.llQuestionsNotAsked.setVisibility(View.VISIBLE);
                        for (TestResultResponse.ResultBodyPart resultBodyPart : response.body().getNotAskedQue()) {
                            View view = LayoutInflater.from(TestResultActivity.this).inflate(R.layout.row_result_body_part, null);
                            ((TextView) view.findViewById(R.id.txtName)).setText(resultBodyPart.getName());
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageResource(R.drawable.circle_green);
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                            binding.llQuestionsNotAsked.addView(view);
                        }
                    } else {
                        binding.txtNoDataQuestionNotAsk.setVisibility(View.VISIBLE);
                        binding.llQuestionsNotAsked.setVisibility(View.GONE);
                    }

                    binding.llAreasOfTheBodyAssessed.removeAllViews();
                    if (response.body().getAccessedBodyParts().size() > 0) {
                        binding.txtNoDataAreaAssessed.setVisibility(View.GONE);
                        binding.llAreasOfTheBodyAssessed.setVisibility(View.VISIBLE);
                        for (TestResultResponse.ResultBodyPart resultBodyPart : response.body().getAccessedBodyParts()) {
                            View view = LayoutInflater.from(TestResultActivity.this).inflate(R.layout.row_result_body_part, null);
                            ((TextView) view.findViewById(R.id.txtName)).setText(resultBodyPart.getName().replace("_", " "));
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageResource(R.drawable.ic_check);
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                            binding.llAreasOfTheBodyAssessed.addView(view);
                        }
                    } else {
                        binding.txtNoDataAreaAssessed.setVisibility(View.VISIBLE);
                        binding.llAreasOfTheBodyAssessed.setVisibility(View.GONE);
                    }

                    binding.llAreasOfTheBodyAssessedNot.removeAllViews();
                    if (response.body().getNotAccessedBodyParts().size() > 0) {
                        binding.txtNoDataAreaNotAssessed.setVisibility(View.GONE);
                        binding.llAreasOfTheBodyAssessedNot.setVisibility(View.VISIBLE);
                        for (TestResultResponse.ResultBodyPart resultBodyPart : response.body().getNotAccessedBodyParts()) {
                            View view = LayoutInflater.from(TestResultActivity.this).inflate(R.layout.row_result_body_part, null);
                            ((TextView) view.findViewById(R.id.txtName)).setText(resultBodyPart.getName().replace("_", " "));
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageResource(R.drawable.ic_cross);
                            ((ImageView) view.findViewById(R.id.imgPartsStatus)).setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                            binding.llAreasOfTheBodyAssessedNot.addView(view);
                        }
                    } else {
                        binding.txtNoDataAreaNotAssessed.setVisibility(View.VISIBLE);
                        binding.llAreasOfTheBodyAssessedNot.setVisibility(View.GONE);
                    }

                    binding.txtsubjectivenotes.setText(response.body().getTest().getSoapWrittenByLearner().getSubjectiveNotes());
                    binding.txtobjectivenotes.setText(response.body().getTest().getSoapWrittenByLearner().getObjectiveNotes());
                    binding.txtassessmentnotes.setText(response.body().getTest().getSoapWrittenByLearner().getAssessmentNotes());
                    binding.txtplannotes.setText(response.body().getTest().getSoapWrittenByLearner().getPlanNotes());

                    binding.txtsubjectivenotesByMe.setText(response.body().getTest().getSoapWrittenByMe().getSubjective());
                    binding.txtobjectivenotesByMe.setText(response.body().getTest().getSoapWrittenByMe().getObjective());
                    binding.txtassessmentnotesByMe.setText(response.body().getTest().getSoapWrittenByMe().getAssessment());
                    binding.txtplannotesByMe.setText(response.body().getTest().getSoapWrittenByMe().getPlan());


                    binding.progressBar.setProgress((int) Math.round(response.body().getTest().getResult()));
                    binding.txtResult.setText(response.body().getTest().getResult() + " %");

                    if (Double.parseDouble(response.body().getAverageScoreLimit()) > response.body().getTest().getResult()) {
                        binding.txtImprovement.setText("Needs Improvement");
                    } else {
                        binding.txtImprovement.setText("Good Job");
                    }
                }
            }

            @Override
            public void onFailure(Call<TestResultResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isfrom.equalsIgnoreCase("examsubmit")) {
            finish();
            backAnimation();
        } else {
            Intent intent = new Intent(TestResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            startAnimation();
            finish();
        }
    }
}