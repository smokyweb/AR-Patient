package com.ar.patient.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ar.patient.R;
import com.ar.patient.adapter.VoiceExamQuestionListAdapter;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityExamHelpBinding;
import com.ar.patient.responsemodel.voiceexam.Definition;

import java.util.ArrayList;

public class ExamHelpActivity extends BaseActivity {

    private ActivityExamHelpBinding binding;
    private VoiceExamQuestionListAdapter adapter;
    private ArrayList<Definition> definitions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_help);

        if (getIntent() != null) {
            definitions = (ArrayList<Definition>) getIntent().getSerializableExtra("defination");
        }

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.recnewsfeed.setLayoutManager(new LinearLayoutManager(ExamHelpActivity.this));
        adapter = new VoiceExamQuestionListAdapter(ExamHelpActivity.this, new VoiceExamQuestionListAdapter.Listner() {
            @Override
            public void onClick(Definition value) {
            }
        });
        binding.recnewsfeed.setAdapter(adapter);

        adapter.addAll(definitions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAnimation();
    }
}