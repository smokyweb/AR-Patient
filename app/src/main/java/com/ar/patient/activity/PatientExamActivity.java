package com.ar.patient.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.ARPatientApplication;
import com.ar.patient.R;
import com.ar.patient.adapter.ChatRecyclerAdapter;
import com.ar.patient.adapter.SymptomsListAdapter;
import com.ar.patient.baseclass.BaseActivity;
import com.ar.patient.databinding.ActivityPatientExamBinding;
import com.ar.patient.fcm.MessageEvent;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Speakerbox;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.Chat;
import com.ar.patient.responsemodel.patientsresponse.BodyPart;
import com.ar.patient.responsemodel.patientsresponse.PatientsListResponse;
import com.ar.patient.responsemodel.patientsresponse.Type;
import com.ar.patient.responsemodel.submitexam.SubmitExamResponse;
import com.ar.patient.responsemodel.voiceexam.Definition;
import com.ar.patient.responsemodel.voiceexam.VoiceExamQuestionResponse;
import com.ar.patient.service.MyService;
import com.ar.patient.uc.SlcButtonView;
import com.ar.patient.uc.SlcTextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientExamActivity extends BaseActivity {

    private final int REQ_CODE = 100;
    Speakerbox speakerbox;
    private ActivityPatientExamBinding binding;
    public Handler handler;
    public Runnable runnable;
    boolean isUp;
    String currentDateandTime;
    private String SpeakingText = "";
    private Type bean;
    private ArrayList<PatientsListResponse.BodyParts> bodyPartsArrayList;
    private ArrayList<Definition> questionlist = new ArrayList<>();
    private ArrayList<String> exceptionalWords = new ArrayList<>();
    private boolean isstartchat = false;
    private String updatedmsg = "";
    private ArrayList<Chat> chatArrayList = new ArrayList<>();
    private ChatRecyclerAdapter adapter;
    private ArrayList<Definition> definitionData = new ArrayList<>();
    private int wrongAnswer = 0;
    private int clickbottom = 0;
    private Map<String, Object> loadModelParameters = new HashMap<>();
    boolean isFront = true;
    long currentMilliSecond;
    int finalHeight, finalWidth;
    int imageWidth = 697, imageHeight = 927;
    DrawView drawView;
    List<BodyPartObject> frontBodyPartObjects = new ArrayList<>();
    List<BodyPartObject> backBodyPartObjects = new ArrayList<>();
    GestureDetector gestureDetector;
    private File front, back;
    private float x1, x2;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScale = 1f;
    private Float mx, my;
    String ids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_exam);

        if (getIntent() != null) {
            bean = (Type) getIntent().getSerializableExtra("Type");
            bodyPartsArrayList = (ArrayList<PatientsListResponse.BodyParts>) getIntent().getSerializableExtra("BodyPartsArrayList");

        }

        initviews();


    }

    private void initviews() {

        File myDir = new File(getExternalCacheDir() + Config.MAINFOLDERNAME);
        if (myDir.exists()) {
            File[] files = myDir.listFiles();
            if (files.length != 0) {
                Log.d("Files", "Size: " + files.length);
                boolean checkFile = false;
                for (File file : files) {
                    if (file.getName().equalsIgnoreCase(Utils.getFileNameFromURL(Config.MEDIA_URL + "/" + bean.getFrontimage()))) {
                        Log.d("mytag", "Front Image Paths : " + file.getAbsolutePath());
                        front = new File(file.getAbsolutePath());
                    } else if (file.getName().equalsIgnoreCase(Utils.getFileNameFromURL(Config.MEDIA_URL + "/" + bean.getBackimage()))) {
                        checkFile = true;
                        Log.d("mytag", "Back Image Paths : " + file.getAbsolutePath());
                        back = new File(file.getAbsolutePath());
                    }
                }
                if (checkFile) {
                    binding.imgMain.setImageBitmap(BitmapFactory.decodeFile(front.getAbsolutePath()));

                }
            }
        }


        if (checkRecordAudioPermission()) {
            setup();
        }

        ViewTreeObserver vto = binding.imgMain.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                binding.imgMain.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = binding.imgMain.getMeasuredHeight();
                finalWidth = binding.imgMain.getMeasuredWidth();

                System.out.println("::::::imageWidth:::::" + imageWidth + "::" + finalWidth);
                System.out.println("::::::imageHeight:::::" + imageHeight + "::" + finalHeight);

                if (isFront) {
                    drawView = new DrawView(PatientExamActivity.this);
                    binding.llDummy.addView(drawView);
                    Canvas canvas = new Canvas();
                    Paint paint = new Paint();
                } else {
                    drawView = new DrawView(PatientExamActivity.this);
                    binding.llDummy.addView(drawView);
                    Canvas canvas = new Canvas();
                    Paint paint = new Paint();
                }
                return true;
            }
        });

        binding.imgSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator on1 = ObjectAnimator.ofFloat(binding.imgMain, "scaleX", 1f, 0f);  // HERE 360 IS THE ANGLE OF ROTATE, YOU CAN USE 90, 180 IN PLACE OF IT,  ACCORDING TO YOURS REQUIREMENT
                ObjectAnimator on2 = ObjectAnimator.ofFloat(binding.imgMain, "scaleX", 0f, 1f);  // HERE 360 IS THE ANGLE OF ROTATE, YOU CAN USE 90, 180 IN PLACE OF IT,  ACCORDING TO YOURS REQUIREMENT
                on1.setDuration(500);
                on2.setDuration(500);

                on1.setInterpolator(new AccelerateDecelerateInterpolator());
                on2.setInterpolator(new AccelerateDecelerateInterpolator());
                on1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (isFront) {
                            binding.imgMain.setImageBitmap(BitmapFactory.decodeFile(back.getAbsolutePath()));
                            drawView = new DrawView(PatientExamActivity.this);
                            binding.llDummy.addView(drawView);
                        } else {
                            binding.imgMain.setImageBitmap(BitmapFactory.decodeFile(front.getAbsolutePath()));
                            drawView = new DrawView(PatientExamActivity.this);
                            binding.llDummy.addView(drawView);
                        }

                        isFront = !isFront;
                        on2.start();
                    }
                });

                on1.start();
            }
        });

        binding.imgMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        currentMilliSecond = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        int secondInDuration = (int) (System.currentTimeMillis() - currentMilliSecond) / 60;

                        if (secondInDuration <= 1) {
                            if (isFront) {
                                for (BodyPartObject bodyPartObject : frontBodyPartObjects) {
                                    if (bodyPartObject.region.contains((int) event.getX(), (int) event.getY())) {
                                        Log.d("mytag", "Body part Name :" + bodyPartObject.partName);
                                        BodyPart bodyPart = null;
                                        for (int i = 0; i < bean.getBodyParts().size(); i++) {
                                            if (bean.getBodyParts().get(i).getName().equalsIgnoreCase(bodyPartObject.partName)) {
                                                bodyPart = bean.getBodyParts().get(i);
                                                break;
                                            }
                                        }

                                        if (bodyPart != null) {
                                            ViewDialog alert = new ViewDialog();
                                            alert.showDialog(PatientExamActivity.this, bodyPartObject.partName.replace("_", " "), bodyPart.getSymptoms());
                                        } else {
                                            ViewDialog alert = new ViewDialog();
                                            alert.showDialog(PatientExamActivity.this, bodyPartObject.partName.replace("_", " "));
                                        }

                                        for (PatientsListResponse.BodyParts bodyParts : bodyPartsArrayList) {
                                            if (bodyParts.getName().toString().trim().toLowerCase().equalsIgnoreCase(bodyPartObject.partName)) {
                                                ids += "," + bodyParts.getId();
                                                Utils.logPrint("::::ids:::::" + ids);
                                                break;
                                            }
                                        }

                                        break;
                                    }
                                }
                            } else {
                                for (BodyPartObject bodyPartObject : backBodyPartObjects) {
                                    if (bodyPartObject.region.contains((int) event.getX(), (int) event.getY())) {
                                        Log.d("mytag", "Body part Name :" + bodyPartObject.partName);
                                        BodyPart bodyPart = null;
                                        for (int i = 0; i < bean.getBodyParts().size(); i++) {
                                            if (bean.getBodyParts().get(i).getName().equalsIgnoreCase(bodyPartObject.partName)) {
                                                bodyPart = bean.getBodyParts().get(i);
                                                break;
                                            }
                                        }

                                        if (bodyPart != null) {
                                            ViewDialog alert = new ViewDialog();
                                            alert.showDialog(PatientExamActivity.this, bodyPartObject.partName.replace("_", " "), bodyPart.getSymptoms());
                                        } else {
                                            ViewDialog alert = new ViewDialog();
                                            alert.showDialog(PatientExamActivity.this, bodyPartObject.partName.replace("_", " "));
                                        }
                                        break;
                                    }
                                }
                            }

                        } else if (Math.abs(deltaX) > 150) {
                            binding.imgSwipe.performClick();
                        }
                        break;

                }

                return true;
            }
        });

    }


    private void setup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());
        Log.d("mytag", "Current time : " + currentDateandTime);


        if (Pref.getValue(PatientExamActivity.this, Config.PREF_IS_HINT_SHOW, "").equalsIgnoreCase("0") ||
                Pref.getValue(PatientExamActivity.this, Config.PREF_IS_HINT_SHOW, "").equalsIgnoreCase("")) {
            binding.relonetimeshowHint.setVisibility(View.VISIBLE);
        } else {
            binding.relonetimeshowHint.setVisibility(View.GONE);
        }

        if (bean.getImageId().equalsIgnoreCase("1") ||
                bean.getImageId().equalsIgnoreCase("2") ||
                bean.getImageId().equalsIgnoreCase("3") ||
                bean.getImageId().equalsIgnoreCase("8")) {
            speakerbox = new Speakerbox(getApplication(), Config.femaleConfig);
        } else {
            speakerbox = new Speakerbox(getApplication(), Config.maleConfig);
        }


        speakerbox.setActivity(PatientExamActivity.this);

        SpeakingText = "Hey ".toLowerCase() + bean.getName().toLowerCase();

        binding.relBottom.setVisibility(View.GONE);
        binding.linBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickbottom == 0) {
                    binding.relBottom.setVisibility(View.VISIBLE);
                    clickbottom = clickbottom + 1;
                    binding.ImgArraow.setRotation(0f);
                } else {
                    binding.relBottom.setVisibility(View.GONE);
                    clickbottom = 0;
                    binding.ImgArraow.setRotation(180f);
                }

            }
        });

        binding.ImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lintip.setVisibility(View.GONE);
            }
        });

        binding.recmsg.setLayoutManager(new LinearLayoutManager(PatientExamActivity.this));

        binding.ImgDignosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(PatientExamActivity.this, DiagnosisActivity.class);
                help.putExtra("chatsize", adapter.getItemCount());
                help.putExtra("patientId", bean.getPatientId());
                help.putExtra("isfrom", "withoutexamsubmit");
                startActivity(help);
                startAnimation();
            }
        });

        binding.ImgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(PatientExamActivity.this, ExamHelpActivity.class);
                help.putExtra("defination", questionlist);
                startActivity(help);
                startAnimation();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getItemCount() > 0) {
                    showDialog(PatientExamActivity.this, "Are you sure want to exit the exam?", new DialogClickListener() {
                        @Override
                        public void onClick() {
                            finish();
                            backAnimation();
                            chatArrayList = new ArrayList<>();
                            Pref.setValue(PatientExamActivity.this, Config.PREF_TEST_ID, "0");
                        }
                    });
                } else {
                    finish();
                    backAnimation();
                    chatArrayList = new ArrayList<>();
                    Pref.setValue(PatientExamActivity.this, Config.PREF_TEST_ID, "0");
                }

            }
        });

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getItemCount() > 0) {
                    showDialog(PatientExamActivity.this, "Are you sure want to submit the exam?", new DialogClickListener() {
                        @Override
                        public void onClick() {

                            Gson gson = new GsonBuilder().create();
                            JsonArray myCustomArray = gson.toJsonTree(definitionData).getAsJsonArray();

                            Log.d("mytag", "myCustomArray : " + myCustomArray);

                            submitExam(myCustomArray);

                        }
                    });
                }
            }
        });

        adapter = new ChatRecyclerAdapter(PatientExamActivity.this, bean.getName(), bean.getAvatar(), new ChatRecyclerAdapter.onAnsweerclicklistener() {
            @Override
            public void onanswerClicked(String bean) {
//                                sendanswer(mainresponse.getQuestions().get(PageNo).getId(), bean);
            }
        });
        binding.recmsg.setAdapter(adapter);

        chatArrayList = new ArrayList<>();
        binding.ImgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.EdtMessege.getText().toString().trim().isEmpty())
                    return;

                Chat chatData = new Chat();
                chatData.setIsme(true);
                chatData.setMessege(binding.EdtMessege.getText().toString());

                adapter.add(chatData);

                calculateanswer(updatedmsg.replace(",", "").replace("?", "").replace(".", "").replace("!", ""));
                binding.recmsg.scrollToPosition(adapter.getItemCount() - 1);
                Utils.hideKeyboard(PatientExamActivity.this, binding.MainLayout);
            }
        });


        binding.EdtMessege.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    updatedmsg = editable.toString();
                    Log.d("mytag", "Updated msg ::: " + updatedmsg);
                }
            }
        });


        binding.txtHintOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.relHint.setVisibility(View.GONE);
            }
        });

        binding.btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.setValue(PatientExamActivity.this, Config.PREF_IS_HINT_SHOW, "1");
                binding.relonetimeshowHint.setVisibility(View.GONE);
            }
        });

        getVoiceExam();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        backAnimation();
        chatArrayList = new ArrayList<>();
        Pref.setValue(PatientExamActivity.this, Config.PREF_TEST_ID, "0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isMyServiceRunning(PatientExamActivity.this, MyService.class)) {
            if (runnable == null) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (checkRecordAudioPermission()) {
                            startVoiceService();
                        }
                    }
                };
            }

            if (handler == null) {
                handler = new Handler();
            }

            handler.postDelayed(runnable, 1000);

        }

        if (binding.relBottom.getVisibility() == View.VISIBLE) {
            binding.ImgArraow.setRotation(0f);
        } else {
            binding.ImgArraow.setRotation(180f);
        }
    }

    public void startVoiceService() {
        getApplication().getBaseContext().
                startService(new Intent(PatientExamActivity.this, MyService.class)
                        .putExtra("text", SpeakingText));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionResult(this, requestCode, permissions, grantResults, new PermissoinsResult() {
            @Override
            public void onReturn(int requestcode) {
                initviews();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (Utils.isMyServiceRunning(this, MyService.class))
            stopService(new Intent(this, MyService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utils.isMyServiceRunning(this, MyService.class))
            stopService(new Intent(this, MyService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(MessageEvent event) {
        Log.d("mytag", "messageEventFromService : " + event.message);

        if (event.message.toLowerCase().contains(SpeakingText.toLowerCase())) {
            binding.ImgGif.setVisibility(View.VISIBLE);
            binding.Imgdot.setVisibility(View.GONE);
            Glide.with(PatientExamActivity.this).load(R.raw.speaking).into(binding.ImgGif);
            isstartchat = true;
            chatArrayList = new ArrayList<>();
        }
        if (isstartchat) {
            if (binding.relBottom.getVisibility() == View.GONE) {
                updatedmsg = "";
                binding.EdtMessege.getText().clear();
                if (event.message.toLowerCase().equalsIgnoreCase(SpeakingText.toLowerCase())) {
                    binding.EdtMessege.setText("");
                } else {
                    binding.EdtMessege.setText(event.message.toLowerCase());
                }

                updatedmsg = binding.EdtMessege.getText().toString();
            }
        }
    }

    private void calculateanswer(String question) {
        Log.d("mytag", "final Question :: " + question);
        int maxCount = 0;

        ArrayList<String> arrAskedQuestion = new ArrayList<>();


        //remove words if less or equal 2 lenght
        for (int i = 0; i < question.toLowerCase().split(" ").length; i++) {
            if (question.toLowerCase().split(" ")[i].length() > 2) {
                arrAskedQuestion.add(question.toLowerCase().split(" ")[i]);
            }
        }


        List<String> arrAskedQuestionAfterRemoveExceptional = new ArrayList<String>();
        for (int k = 0; k < arrAskedQuestion.size(); k++) {
            if (!exceptionalWords.contains(arrAskedQuestion.get(k).toLowerCase())) {

                arrAskedQuestionAfterRemoveExceptional.add(arrAskedQuestion.get(k).toLowerCase());
            }

        }

        ArrayList<QuestionWithMatchcount> questionWithMatchcounts = new ArrayList<>();
        for (int i = 0; i < questionlist.size(); i++) {

            String arrQuestion[] = questionlist.get(i).getQuestion().toLowerCase().replace(",", "")
                    .replace("?", "").replace(".", "")
                    .replace("!", "").split(" ");

            List<String> arrQuestionAfterRemoveExceptional = new ArrayList<String>();


            for (int j = 0; j < arrQuestion.length; j++) {
                if (!exceptionalWords.contains(arrQuestion[j].toLowerCase())) {
                    arrQuestionAfterRemoveExceptional.add(arrQuestion[j].toLowerCase());
                }
            }

            Double matchWords = 0.0;
            for (int i1 = 0; i1 < arrAskedQuestionAfterRemoveExceptional.size(); i1++) {
                if (arrQuestionAfterRemoveExceptional.contains(arrAskedQuestionAfterRemoveExceptional.get(i1))) {
                    matchWords += 1;
                }
            }

            QuestionWithMatchcount questionWithMatchcount = new QuestionWithMatchcount();
            questionWithMatchcount.wordMatchCout = matchWords;
            questionWithMatchcount.definition = questionlist.get(i);
            questionWithMatchcounts.add(questionWithMatchcount);


        }

        //sort by match words counter
        Collections.sort(questionWithMatchcounts, new Comparator<QuestionWithMatchcount>() {
            @Override
            public int compare(QuestionWithMatchcount o1, QuestionWithMatchcount o2) {
                return o2.wordMatchCout.compareTo(o1.wordMatchCout);
            }
        });

        if (questionWithMatchcounts.get(0).wordMatchCout == 0) {

            wrongAnswer = wrongAnswer + 1;

            Definition definition = new Definition();
            definition.setAnswer("");
            definition.setId("0");
            definition.setQuestion(question);
            definition.setIsMandatory("");
            definitionData.add(definition);

            Chat chatData = new Chat();
            chatData.setIsme(false);
            chatData.setMessege(getString(R.string.can_you_ask_diff_question));
            adapter.add(chatData);
            if (wrongAnswer >= 3) {
                if (bean.getHintText() != "") {
                    speakerbox.play(bean.getHintText());
                    binding.relHint.setVisibility(View.VISIBLE);
                    binding.txthint.setText(bean.getHintText());
                    wrongAnswer = 0;
                    binding.EdtMessege.getText().clear();
                }
            } else {
                speakerbox.play(getString(R.string.can_you_ask_diff_question));
                if (clickbottom == 0) {
                    binding.relBottom.setVisibility(View.VISIBLE);
                    clickbottom = clickbottom + 1;
                } else {
                    binding.relBottom.setVisibility(View.GONE);
                    clickbottom = 0;
                }

                binding.EdtMessege.getText().clear();

                if (chatArrayList.size() == 2) {
                    adapter.addall(chatArrayList);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

        } else {

            //remove other ques
            double count = questionWithMatchcounts.get(0).wordMatchCout;
            for (int i = 0; i < questionWithMatchcounts.size(); i++) {
                if (questionWithMatchcounts.get(i).wordMatchCout != count) {
                    questionWithMatchcounts.remove(i);
                    i--;
                }
            }

            ArrayList<QuestionWithMatchcount> ratioWithQuestion = new ArrayList<>();
            for (QuestionWithMatchcount questionWithMatchcount : questionWithMatchcounts) {
                String newQues = questionWithMatchcount.definition.getQuestion().toLowerCase().replace(",", "")
                        .replace("?", "").replace(".", "")
                        .replace("!", "");
                String quest[] = newQues.split(" ");
                for (int i = 0; i < quest.length; i++) {
                    quest[i] = quest[i].toLowerCase();
                }
                int noOfMatch = 0;
                for (int i = 0; i < question.toLowerCase().split(" ").length; i++) {

                    if (Arrays.asList(quest).contains(question.toLowerCase().split(" ")[i])) {
                        noOfMatch += 1;
                    }
                }
                double ratio = Double.valueOf(noOfMatch / (double) newQues.split(" ").length);
                QuestionWithMatchcount questionWithMatchcount1 = new QuestionWithMatchcount();
                questionWithMatchcount1.wordMatchCout = ratio;
                questionWithMatchcount1.definition = questionWithMatchcount.definition;
                ratioWithQuestion.add(questionWithMatchcount1);
            }

            Collections.sort(ratioWithQuestion, new Comparator<QuestionWithMatchcount>() {
                @Override
                public int compare(QuestionWithMatchcount o1, QuestionWithMatchcount o2) {
                    return o2.wordMatchCout.compareTo(o1.wordMatchCout);
                }
            });

            definitionData.add(ratioWithQuestion.get(0).definition);

            Chat chatData = new Chat();
            chatData.setIsme(false);
            chatData.setMessege(ratioWithQuestion.get(0).definition.getAnswer());

            speakerbox.play(ratioWithQuestion.get(0).definition.getAnswer());

            adapter.add(chatData);

            if (clickbottom == 0) {
                binding.relBottom.setVisibility(View.VISIBLE);
                clickbottom = clickbottom + 1;
            } else {
                binding.relBottom.setVisibility(View.GONE);
                clickbottom = 0;
            }

            binding.EdtMessege.getText().clear();

            if (chatArrayList.size() == 2) {
                adapter.addall(chatArrayList);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void getVoiceExam() {
        if (!isOnline())
            return;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(PatientExamActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("patient_id", "" + bean.getPatientId())
                .build();

        ARPatientApplication.getRestClient().getApiService().getvoiceExam(requestBody).enqueue(new Callback<VoiceExamQuestionResponse>() {
            @Override
            public void onResponse(Call<VoiceExamQuestionResponse> call, Response<VoiceExamQuestionResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        questionlist = response.body().getDefinition();
                        exceptionalWords = response.body().getExceptionalWords();
                    } else {
                        showValidationDialog(PatientExamActivity.this, response.body().getMessage(), null);
                    }

                } else {
                    showValidationDialog(PatientExamActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<VoiceExamQuestionResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void submitExam(JsonArray myCustomArray) {
        if (!isOnline())
            return;


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "" + Pref.getValue(PatientExamActivity.this, Config.PREF_USERID, ""))
                .addFormDataPart("patient_id", "" + bean.getPatientId())
                .addFormDataPart("test_id", "" + Pref.getValue(PatientExamActivity.this, Config.PREF_TEST_ID, ""))
                .addFormDataPart("sys_time", "" + currentDateandTime)
                .addFormDataPart("conversations", "" + myCustomArray)
                .addFormDataPart("body_part_ids", "" + (ids.isEmpty() ? "" : ids.substring(1)))
                .build();


        ARPatientApplication.getRestClient().getApiService().submitExam(requestBody).enqueue(new Callback<SubmitExamResponse>() {
            @Override
            public void onResponse(Call<SubmitExamResponse> call, Response<SubmitExamResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase("0")) {
                        Pref.setValue(PatientExamActivity.this, Config.PREF_TEST_ID, response.body().getTestId());
                        Intent help = new Intent(PatientExamActivity.this, DiagnosisActivity.class);
                        help.putExtra("chatsize", adapter.getItemCount());
                        help.putExtra("patientId", bean.getPatientId());
                        help.putExtra("isfrom", "examsubmit");
                        startActivity(help);
                        startAnimation();
                    } else {
                        showValidationDialog(PatientExamActivity.this, response.body().getMessage(), null);
                    }
                } else {
                    showValidationDialog(PatientExamActivity.this, response.body().getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<SubmitExamResponse> call, Throwable t) {

            }
        });
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public Region convertRegion(Canvas canvas, Paint paint, int leftX, int leftY, int topX, int topY, int rightX, int rightY, int bottomX, int bottomY) {
        Region r;
        Path path = new Path();
        path.moveTo(convertFromBaseWidth(leftX), convertFromBaseHeight(leftY));
        path.lineTo(convertFromBaseWidth(topX), convertFromBaseHeight(topY));
        path.lineTo(convertFromBaseWidth(rightX), convertFromBaseHeight(rightY));
        path.lineTo(convertFromBaseWidth(bottomX), convertFromBaseHeight(bottomY));
        path.lineTo(convertFromBaseWidth(leftX), convertFromBaseHeight(leftY));
        path.close();
        /*canvas.drawPath(path, paint);*/
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        r = new Region();
        r.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return r;
    }

/*    public Region convertRegionWithList(Canvas canvas, Paint paint) {
        Region r;
        Path path = new Path();
        path.moveTo(convertFromBaseWidth(335), convertFromBaseHeight(49));

        path.lineTo(convertFromBaseWidth(288), convertFromBaseHeight(54));
        path.lineTo(convertFromBaseWidth(344), convertFromBaseHeight(37));
        path.lineTo(convertFromBaseWidth(402), convertFromBaseHeight(46));
        path.lineTo(convertFromBaseWidth(415), convertFromBaseHeight(114));
        path.lineTo(convertFromBaseWidth(408), convertFromBaseHeight(175));
        path.lineTo(convertFromBaseWidth(434), convertFromBaseHeight(211));
        path.lineTo(convertFromBaseWidth(451), convertFromBaseHeight(233));
        path.lineTo(convertFromBaseWidth(476), convertFromBaseHeight(271));
        path.lineTo(convertFromBaseWidth(500), convertFromBaseHeight(299));
        path.lineTo(convertFromBaseWidth(593), convertFromBaseHeight(397));
        path.lineTo(convertFromBaseWidth(628), convertFromBaseHeight(485));
        path.lineTo(convertFromBaseWidth(535), convertFromBaseHeight(428));
        path.lineTo(convertFromBaseWidth(442), convertFromBaseHeight(393));
        path.lineTo(convertFromBaseWidth(444), convertFromBaseHeight(709));
        path.lineTo(convertFromBaseWidth(456), convertFromBaseHeight(856));
        path.lineTo(convertFromBaseWidth(395), convertFromBaseHeight(863));
        path.lineTo(convertFromBaseWidth(381), convertFromBaseHeight(743));
        path.lineTo(convertFromBaseWidth(294), convertFromBaseHeight(740));
        path.lineTo(convertFromBaseWidth(279), convertFromBaseHeight(876));
        path.lineTo(convertFromBaseWidth(220), convertFromBaseHeight(872));
        path.lineTo(convertFromBaseWidth(236), convertFromBaseHeight(630));
        path.lineTo(convertFromBaseWidth(250), convertFromBaseHeight(391));
        path.lineTo(convertFromBaseWidth(92), convertFromBaseHeight(453));
        path.lineTo(convertFromBaseWidth(154), convertFromBaseHeight(325));
        path.lineTo(convertFromBaseWidth(261), convertFromBaseHeight(192));
        path.lineTo(convertFromBaseWidth(270), convertFromBaseHeight(55));


        path.lineTo(convertFromBaseWidth(335), convertFromBaseHeight(49));
        path.close();
        canvas.drawPath(path, paint);
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        r = new Region();
        r.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return r;
    }*/

    public int convertFromBaseWidth(int points) {
        return (finalWidth * points) / imageWidth;
    }

    public int convertFromBaseHeight(int points) {
        return (finalHeight * points) / imageHeight;
    }

    public void showAlert(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(name);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public class DrawView extends View {
        Paint paint = new Paint();


        public DrawView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(2);

            if (bean.getImageId().equalsIgnoreCase("1")) {
                GirlY18Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("2")) {
                Women55Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("3")) {
                GirlY8Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("4")) {
                BoyY8Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("5")) {
                Man55Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("6")) {
                BoyY18Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("7")) {
                ManY30Points(canvas, paint);
            } else if (bean.getImageId().equalsIgnoreCase("8")) {
                GirlY30Points(canvas, paint);
            }

        }

    }

    public class BodyPartObject {
        public String partName;
        public Region region;

        public BodyPartObject(String partName, Region region) {
            this.partName = partName;
            this.region = region;

        }
    }

    public class ViewDialog {
        public void showDialog(Activity activity, String msg, ArrayList<String> symptoms) {
            final Dialog dialog = new Dialog(activity);
            SymptomsListAdapter adapter;

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_body_parts_symptoms);

            SlcTextView title = dialog.findViewById(R.id.txttitle);
            SlcTextView txtMsg = dialog.findViewById(R.id.txtMsg);
            SlcButtonView dialogButton = dialog.findViewById(R.id.positiveBtn);
            RecyclerView recsymptoms = dialog.findViewById(R.id.recsymptoms);

            title.setText("symptoms for " + " " + msg);

            if (symptoms.size() == 0) {
                recsymptoms.setVisibility(View.GONE);
                txtMsg.setVisibility(View.VISIBLE);
            } else {
                recsymptoms.setVisibility(View.VISIBLE);
                txtMsg.setVisibility(View.GONE);
            }


            recsymptoms.setLayoutManager(new LinearLayoutManager(PatientExamActivity.this));

            adapter = new SymptomsListAdapter(PatientExamActivity.this);

            recsymptoms.setAdapter(adapter);

            adapter.addAll(symptoms);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            SymptomsListAdapter adapter;

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_body_parts_symptoms);

            SlcTextView title = dialog.findViewById(R.id.txttitle);
            SlcTextView txtMsg = dialog.findViewById(R.id.txtMsg);
            SlcButtonView dialogButton = dialog.findViewById(R.id.positiveBtn);
            RecyclerView recsymptoms = dialog.findViewById(R.id.recsymptoms);

            title.setText(msg);

            recsymptoms.setVisibility(View.GONE);
            txtMsg.setVisibility(View.VISIBLE);


            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    // DONE Verify
    public void BoyY8Points(Canvas canvas, Paint paint) {
//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 295, 52, 396, 51, 404, 95, 291, 100)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 310, 88, 392, 87, 393, 115, 304, 116)));
        frontBodyPartObjects.add(new BodyPartObject("Ear_Right", convertRegion(canvas, paint, 286, 116, 307, 116, 313, 153, 297, 155)));
        frontBodyPartObjects.add(new BodyPartObject("Ear_Left", convertRegion(canvas, paint, 386, 113, 405, 116, 393, 157, 380, 154)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 332, 165, 362, 167, 360, 208, 333, 204)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 327, 166, 367, 166, 375, 185, 321, 186)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 310, 181, 383, 181, 389, 211, 301, 208)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 324, 151, 368, 149, 367, 166, 327, 166)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 343, 111, 357, 114, 359, 143, 337, 144)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 312, 109, 346, 109, 344, 125, 315, 126)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 354, 108, 386, 107, 382, 123, 357, 125)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 309, 91, 385, 89, 383, 173, 314, 174)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 293, 50, 411, 50, 411, 200, 293, 200)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 249, 212, 309, 188, 322, 250, 271, 281)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 382, 186, 456, 234, 430, 298, 381, 151)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 113, 383, 240, 223, 262, 310, 136, 414)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 444, 224, 583, 387, 556, 418, 428, 306)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 110, 380, 170, 311, 215, 357, 135, 414)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 510, 306, 582, 387, 554, 419, 450, 357)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 114, 391, 126, 383, 139, 407, 131, 413)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 571, 384, 583, 399, 563, 414, 552, 407)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 73, 468, 106, 405, 139, 428, 91, 472)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 555, 429, 597, 412, 622, 479, 602, 474)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 110, 388, 150, 422, 102, 472, 65, 478)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 581, 385, 641, 475, 611, 486, 543, 428)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 270, 260, 335, 258, 336, 304, 275, 333)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 360, 244, 415, 257, 420, 319, 356, 319)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 321, 367, 365, 380, 370, 423, 318, 420)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 285, 245, 330, 242, 337, 317, 279, 326)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 366, 240, 400, 246, 412, 333, 358, 337)));

        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 340, 261, 386, 260, 375, 313, 331, 316)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 228, 855, 266, 854, 267, 870, 228, 870)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 409, 863, 444, 855, 448, 869, 407, 881)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 236, 795, 267, 388, 267, 807, 236, 807)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 400, 393, 440, 397, 440, 815, 400, 815)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 230, 785, 272, 785, 271, 875, 219, 875)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 401, 789, 438, 789, 455, 874, 400, 880)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 222, 841, 273, 847, 273, 877, 222, 873)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 396, 855, 449, 852, 449, 879, 396, 880)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 242, 644, 290, 644, 290, 665, 242, 665)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 391, 651, 436, 648, 434, 671, 385, 673)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 254, 515, 310, 526, 290, 644, 242, 644)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 366, 512, 427, 509, 436, 648, 391, 651)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 242, 665, 290, 665, 272, 785, 230, 785)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 385, 673, 434, 671, 438, 789, 401, 789)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 254, 394, 428, 394, 428, 494, 254, 494)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 254, 394, 428, 394, 428, 434, 254, 434)));

        frontBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 258, 442, 297, 443, 284, 556, 253, 560)));
        frontBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 389, 465, 426, 463, 430, 559, 398, 557)));

        //back parts

        backBodyPartObjects.add(new BodyPartObject("Ear_Right", convertRegion(canvas, paint, 388, 124, 398, 122, 387, 159, 373, 157)));
        backBodyPartObjects.add(new BodyPartObject("Ear_Left", convertRegion(canvas, paint, 287, 125, 306, 120, 315, 158, 303, 159)));
        backBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 315, 168, 372, 165, 378, 184, 312, 192)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 295, 52, 396, 51, 392, 168, 305, 167)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 381, 181, 449, 224, 427, 299, 371, 235)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 246, 218, 310, 184, 325, 235, 277, 307)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 472, 266, 511, 317, 470, 357, 446, 325)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 174, 314, 202, 290, 246, 314, 210, 343)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 511, 317, 544, 376, 530, 398, 470, 357)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 137, 378, 174, 314, 210, 343, 164, 399)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 543, 373, 548, 392, 531, 397, 520, 387)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 136, 391, 146, 379, 165, 388, 159, 401)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 330, 171, 360, 168, 366, 212, 326, 217)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 324, 234, 373, 248, 377, 385, 333, 395)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 333, 395, 377, 385, 387, 440, 327, 441)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 418, 820, 446, 813, 451, 866, 417, 866)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 241, 817, 267, 812, 268, 863, 238, 863)));
        backBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 268, 400, 431, 400, 433, 475, 264, 475)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 262, 485, 432, 485, 434, 546, 257, 546)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 393, 446, 430, 446, 435, 545, 392, 545)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 267, 447, 302, 447, 297, 542, 258, 542)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 390, 547, 435, 547, 448, 682, 398, 682)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 261, 527, 314, 533, 296, 649, 246, 649)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 395, 703, 447, 697, 449, 821, 416, 821)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 242, 693, 288, 691, 272, 820, 242, 821)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 407, 818, 452, 823, 456, 864, 418, 864)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 236, 818, 280, 818, 288, 863, 232, 865)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 506, 395, 548, 385, 562, 438, 546, 443)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 133, 401, 165, 389, 182, 404, 125, 443)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 503, 397, 552, 384, 569, 440, 537, 445)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 116, 435, 143, 368, 187, 403, 139, 452)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 59, 399, 59, 399, 196, 292, 196)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 440, 220, 572, 439, 545, 442, 408, 261)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 109, 434, 253, 212, 277, 270, 131, 450)));
        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 277, 196, 429, 192, 428, 434, 270, 446)));
    }

    //DONE Verify
    public void BoyY18Points(Canvas canvas, Paint paint) {

//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 52, 399, 52, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 325, 81, 380, 81, 380, 96, 325, 96)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 302, 109, 315, 106, 321, 135, 311, 137)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 384, 107, 397, 105, 389, 136, 378, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 341, 165, 364, 164, 361, 187, 337, 186)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 335, 147, 369, 147, 369, 158, 335, 158)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 324, 161, 375, 161, 377, 183, 322, 182)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 332, 132, 368, 132, 368, 146, 332, 146)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 349, 101, 356, 102, 360, 125, 340, 128)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 324, 92, 351, 94, 349, 111, 323, 111)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 93, 378, 94, 378, 109, 358, 110)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 322, 82, 380, 82, 380, 158, 322, 158)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 282, 41, 411, 41, 411, 174, 282, 174)));


        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 259, 202, 325, 179, 324, 259, 271, 263)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 377, 175, 445, 204, 441, 273, 400, 271)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 44, 465, 249, 196, 267, 284, 68, 517)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 102, 400, 165, 303, 207, 330, 124, 427)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 449, 198, 642, 486, 611, 505, 428, 276)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 500, 342, 526, 313, 590, 407, 566, 421)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 104, 413, 115, 402, 134, 415, 123, 430)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 562, 410, 580, 395, 589, 408, 568, 421)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 63, 489, 97, 426, 128, 442, 69, 502)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 567, 439, 601, 429, 633, 486, 611, 492)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 55, 475, 98, 405, 145, 433, 66, 511)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 560, 434, 598, 411, 640, 486, 594, 495)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 276, 220, 346, 214, 347, 351, 259, 359)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 354, 224, 430, 215, 428, 381, 357, 388)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 326, 331, 380, 331, 387, 389, 326, 389)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 300, 230, 346, 230, 346, 312, 300, 312)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 368, 230, 403, 233, 404, 332, 359, 327)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 234, 857, 278, 860, 266, 877, 233, 872)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 431, 858, 465, 839, 471, 863, 437, 871)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 266, 801, 291, 802, 291, 835, 257, 831)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 409, 807, 434, 801, 442, 826, 408, 834)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 227, 860, 262, 817, 297, 823, 267, 883)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 398, 823, 437, 807, 472, 853, 445, 880)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 237, 855, 276, 863, 266, 880, 233, 869)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 428, 860, 462, 841, 472, 861, 442, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 265, 627, 316, 629, 310, 680, 263, 683)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 390, 652, 437, 635, 441, 688, 390, 699)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 263, 683, 310, 680, 291, 802, 266, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 699, 441, 688, 434, 801, 409, 807)));
        frontBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 259, 468, 293, 479, 290, 557, 263, 565)));
        frontBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 401, 485, 433, 481, 437, 563, 409, 561)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 264, 500, 324, 506, 316, 629, 265, 627)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 384, 519, 437, 515, 437, 635, 390, 652)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 292, 373, 412, 383, 431, 489, 280, 491)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 262, 424, 431, 410, 431, 452, 262, 466)));
        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 352, 249, 403, 248, 400, 303, 361, 301)));

        //back
        backBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 385, 104, 395, 105, 390, 133, 378, 138)));
        backBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 297, 109, 309, 102, 322, 136, 308, 137)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 307, 49, 383, 49, 377, 146, 324, 146)));
        backBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 317, 153, 376, 152, 380, 176, 317, 172)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 284, 44, 403, 44, 403, 172, 284, 172)));

        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 481, 324, 519, 289, 539, 318, 508, 351)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 161, 315, 182, 297, 206, 331, 185, 350)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 546, 446, 608, 423, 650, 500, 626, 509)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 54, 485, 89, 424, 127, 444, 68, 510)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 576, 420, 594, 406, 604, 420, 583, 432)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 94, 418, 107, 403, 123, 416, 114, 434)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 560, 438, 603, 403, 648, 496, 618, 516)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 48, 490, 97, 401, 139, 435, 65, 514)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 509, 353, 541, 320, 602, 406, 578, 424)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 97, 409, 157, 313, 193, 349, 115, 435)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 430, 261, 450, 198, 670, 503, 619, 517)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 34, 485, 243, 192, 267, 287, 59, 520)));

        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 333, 380, 376, 379, 374, 422, 328, 422)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 331, 188, 370, 188, 375, 260, 332, 260)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 334, 274, 375, 270, 380, 362, 332, 367)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 375, 171, 451, 190, 421, 291, 389, 284)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 247, 203, 319, 170, 321, 270, 265, 286)));
        backBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 261, 450, 442, 451, 441, 491, 262, 499)));
        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 274, 214, 426, 217, 441, 485, 262, 486)));

        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 398, 487, 442, 486, 439, 564, 399, 564)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 263, 496, 316, 497, 307, 573, 260, 574)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 263, 471, 439, 466, 436, 537, 260, 545)));

        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 411, 830, 435, 822, 437, 876, 408, 878)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 259, 824, 288, 825, 292, 876, 259, 869)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 408, 834, 438, 819, 466, 839, 416, 885)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 229, 839, 286, 812, 297, 870, 265, 880)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 388, 698, 439, 683, 442, 829, 407, 832)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 255, 680, 302, 684, 291, 826, 261, 823)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 376, 553, 432, 546, 440, 672, 387, 681)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 263, 529, 315, 531, 306, 675, 261, 673)));

    }

    // DONE Verify
    public void GirlY8Points(Canvas canvas, Paint paint) {
//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 52, 399, 52, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 300, 79, 392, 79, 392, 110, 304, 110)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 290, 130, 301, 130, 306, 158, 294, 156)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 387, 130, 398, 130, 400, 158, 388, 156)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 208, 364, 208, 364, 217, 329, 217)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 175, 369, 175, 369, 197, 322, 197)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 328, 163, 364, 163, 364, 175, 328, 175)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 332, 134, 364, 134, 364, 156, 332, 156)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 313, 119, 337, 119, 337, 131, 313, 131)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 119, 382, 119, 382, 131, 356, 131)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 81, 392, 81, 392, 198, 306, 198)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 213, 296, 248, 231, 324, 206, 276)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 380, 248, 430, 213, 490, 278, 443, 328)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 230, 260, 325, 205, 368, 168, 331)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 331, 205, 368, 142, 422, 114, 392)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 432, 323, 453, 230, 522, 326, 486, 364)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 364, 522, 326, 579, 398, 550, 418)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 123, 391, 150, 415, 138, 428, 110, 402)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 545, 405, 570, 390, 581, 401, 556, 420)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 92, 430, 116, 451, 90, 486, 75, 467)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 571, 443, 600, 428, 618, 474, 602, 482)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 114, 401, 150, 431, 96, 493, 64, 459)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 542, 427, 580, 401, 627, 467, 592, 489)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 279, 340, 278, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 279, 420, 282, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 413, 371, 413, 371, 446, 320, 446)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 261, 868, 272, 868, 272, 876, 261, 876)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 424, 868, 432, 868, 432, 876, 425, 876)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 239, 780, 279, 780, 279, 801, 239, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 417, 786, 455, 786, 455, 803, 417, 803)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 240, 808, 280, 808, 282, 861, 230, 854)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 417, 811, 454, 808, 466, 846, 412, 857)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 230, 854, 284, 860, 274, 881, 230, 868)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 416, 859, 464, 848, 466, 866, 424, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 240, 647, 297, 646, 286, 733, 231, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 390, 648, 451, 648, 462, 730, 405, 745)));


        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 231, 722, 293, 723, 280, 795, 240, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 400, 720, 459, 709, 454, 793, 416, 794)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 289, 688, 231, 687)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 460, 674, 391, 690)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));


        //Back
        backBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 387, 120, 398, 120, 400, 158, 388, 156)));
        backBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 290, 120, 301, 120, 306, 158, 294, 156)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 152, 304, 152)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));


        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 450, 347, 504, 296, 543, 337, 484, 390)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 190, 305, 230, 350, 194, 382, 153, 345)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 502, 405, 513, 390, 563, 424, 565, 446)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 123, 427, 173, 394, 185, 402, 133, 447)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 432, 303, 435, 213, 522, 326, 486, 364)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 257, 220, 260, 325, 205, 368, 168, 331)));

        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));
        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));

        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));


        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 417, 786, 455, 786, 455, 803, 417, 803)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 239, 780, 279, 780, 279, 801, 239, 801)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 400, 720, 459, 709, 454, 793, 416, 794)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 231, 722, 293, 723, 280, 795, 240, 797)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 460, 674, 391, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 289, 688, 231, 687)));


    }

    // DONE Verify
    public void GirlY18Points(Canvas canvas, Paint paint) {
//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 52, 399, 52, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 300, 79, 392, 79, 392, 100, 304, 100)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 310, 130, 321, 130, 320, 158, 310, 156)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 377, 130, 390, 130, 390, 158, 378, 156)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 178, 364, 178, 364, 207, 329, 207)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 155, 369, 155, 369, 169, 322, 169)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 330, 137, 364, 137, 364, 155, 330, 155)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 340, 104, 359, 104, 359, 136, 340, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 315, 100, 342, 100, 342, 120, 315, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 100, 382, 100, 382, 120, 356, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 81, 392, 81, 392, 178, 306, 178)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 230, 260, 325, 205, 350, 168, 321)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 321, 205, 350, 123, 390, 110, 372)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 435, 313, 453, 230, 522, 310, 486, 364)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 550, 390)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 95, 359, 130, 390, 128, 418, 85, 380)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 555, 390, 590, 359, 610, 390, 576, 420)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 102, 400, 116, 431, 80, 470, 55, 437)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 571, 413, 600, 395, 658, 454, 622, 462)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 95, 380, 130, 401, 96, 476, 55, 452)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 572, 407, 620, 392, 657, 447, 592, 469)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 279, 340, 278, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 279, 420, 282, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 413, 371, 413, 371, 446, 320, 446)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 273, 858, 284, 858, 284, 866, 273, 866)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 414, 858, 425, 858, 425, 869, 415, 869)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 259, 780, 310, 780, 310, 801, 259, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 387, 786, 435, 786, 435, 803, 387, 803)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 268, 808, 300, 808, 300, 861, 248, 854)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 390, 811, 434, 808, 456, 846, 397, 857)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 250, 854, 304, 860, 294, 881, 250, 868)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 400, 859, 464, 848, 466, 866, 404, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 260, 647, 310, 646, 310, 723, 260, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 380, 648, 431, 638, 442, 730, 390, 740)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 256, 722, 323, 723, 310, 795, 266, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 720, 439, 709, 434, 793, 390, 794)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 323, 723, 256, 722)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 439, 709, 390, 720)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));

//BACK -Y18---//

        backBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 172, 300, 172)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 460, 347, 504, 286, 543, 327, 494, 390)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 149, 326, 176, 291, 235, 347, 194, 382)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 550, 390)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 168, 321, 205, 350, 123, 390, 110, 372)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 555, 390, 590, 359, 610, 390, 576, 420)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 95, 359, 130, 390, 128, 418, 85, 380)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 542, 425, 553, 400, 620, 424, 620, 456)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 82, 427, 153, 392, 155, 422, 82, 457)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 432, 310, 425, 207, 525, 306, 486, 374)));
        backBodyPartObjects.add(new BodyPartObject("Arm_left", convertRegion(canvas, paint, 257, 210, 260, 325, 205, 368, 168, 301)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 390, 786, 435, 786, 435, 813, 390, 813)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 259, 780, 299, 780, 299, 811, 259, 811)));
        backBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 440, 684, 381, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 313, 688, 251, 687)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 380, 700, 439, 690, 434, 793, 396, 794)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 251, 702, 313, 703, 300, 795, 250, 797)));


        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));


    }

    private void ManY30Points(Canvas canvas, Paint paint) {

//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 52, 399, 52, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 300, 79, 392, 79, 392, 100, 304, 100)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 300, 120, 321, 120, 320, 148, 300, 146)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 387, 120, 399, 120, 390, 148, 378, 146)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 178, 364, 178, 364, 207, 329, 207)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 155, 369, 155, 369, 169, 322, 169)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 330, 137, 364, 137, 364, 155, 330, 155)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 340, 104, 359, 104, 359, 136, 340, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 315, 100, 342, 100, 342, 120, 315, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 100, 382, 100, 382, 120, 356, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 81, 392, 81, 392, 178, 306, 178)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 230, 260, 300, 205, 350, 168, 321)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 435, 313, 453, 230, 522, 310, 486, 364)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 321, 210, 350, 153, 410, 115, 392)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 523, 395)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 120, 359, 169, 390, 152, 418, 105, 380)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 525, 390, 570, 359, 590, 390, 546, 420)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 95, 427, 130, 471, 130, 510, 75, 510)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 562, 437, 620, 432, 657, 500, 572, 510)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 102, 400, 156, 431, 115, 525, 55, 497)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 561, 459, 600, 415, 650, 489, 602, 515)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 279, 340, 278, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 279, 420, 282, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 413, 371, 413, 371, 446, 320, 446)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 253, 860, 264, 860, 264, 874, 253, 874)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 430, 862, 440, 862, 440, 876, 430, 876)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 259, 780, 310, 780, 310, 801, 259, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 377, 786, 435, 786, 435, 803, 377, 803)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 268, 808, 300, 808, 300, 861, 228, 854)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 389, 811, 434, 808, 456, 846, 397, 857)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 220, 854, 280, 860, 280, 881, 220, 868)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 400, 859, 464, 848, 466, 876, 410, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 260, 647, 310, 646, 310, 723, 260, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 380, 648, 431, 638, 442, 730, 390, 740)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 323, 723, 256, 722)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 439, 709, 390, 720)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 256, 722, 323, 723, 310, 795, 266, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 720, 439, 709, 434, 793, 390, 794)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));


        frontBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        frontBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));


//Back

        backBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 172, 300, 172)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 149, 326, 176, 291, 235, 347, 194, 382)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 460, 347, 504, 286, 543, 327, 494, 390)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 168, 321, 210, 350, 153, 410, 115, 392)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 523, 395)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 120, 359, 169, 390, 152, 418, 105, 380)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 525, 390, 570, 359, 590, 390, 546, 420)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 257, 210, 260, 325, 205, 368, 168, 301)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 432, 310, 425, 207, 525, 306, 486, 374)));
        backBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 313, 688, 251, 687)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 440, 684, 381, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 251, 702, 313, 703, 310, 795, 250, 797)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 380, 700, 439, 690, 434, 793, 390, 794)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 72, 450, 133, 430, 130, 487, 55, 482)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 565, 469, 555, 430, 625, 444, 645, 486)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 102, 400, 156, 431, 115, 525, 55, 497)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 561, 459, 600, 415, 650, 489, 602, 515)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 249, 780, 299, 780, 299, 811, 249, 811)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 390, 786, 435, 786, 445, 813, 395, 813)));
        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));

    }

    private void GirlY30Points(Canvas canvas, Paint paint) {
        //girl----30---front---//
//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 52, 399, 52, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 300, 79, 392, 79, 392, 100, 304, 100)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 300, 120, 321, 120, 320, 148, 300, 146)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 387, 120, 399, 120, 390, 148, 378, 146)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 178, 364, 178, 364, 207, 329, 207)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 155, 369, 155, 369, 169, 322, 169)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 330, 137, 364, 137, 364, 155, 330, 155)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 340, 104, 359, 104, 359, 136, 340, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 315, 100, 342, 100, 342, 120, 315, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 100, 382, 100, 382, 120, 356, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 81, 392, 81, 392, 178, 306, 178)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 230, 260, 300, 205, 350, 168, 321)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 435, 313, 453, 230, 522, 310, 486, 364)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 321, 205, 350, 153, 400, 110, 372)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 550, 390)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 95, 359, 152, 390, 142, 418, 85, 380)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 545, 390, 590, 359, 610, 390, 566, 420)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 95, 405, 130, 451, 96, 476, 55, 472)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 572, 437, 620, 432, 657, 467, 592, 489)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 102, 400, 116, 431, 80, 490, 55, 457)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 571, 433, 600, 395, 658, 454, 622, 482)));


        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 279, 340, 278, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 279, 420, 282, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 413, 371, 413, 371, 446, 320, 446)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));


        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 273, 858, 284, 858, 284, 872, 273, 872)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 400, 858, 417, 858, 417, 872, 400, 872)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 259, 780, 310, 780, 310, 801, 259, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 377, 786, 435, 786, 435, 803, 377, 803)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 268, 808, 300, 808, 300, 861, 248, 854)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 380, 811, 424, 808, 446, 846, 387, 857)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 250, 854, 304, 860, 294, 881, 250, 868)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 390, 859, 464, 848, 466, 866, 404, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 260, 647, 310, 646, 310, 723, 260, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_left", convertRegion(canvas, paint, 380, 648, 431, 638, 442, 730, 390, 740)));


        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 323, 723, 256, 722)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 439, 709, 390, 720)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 256, 722, 323, 723, 310, 795, 266, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 720, 439, 709, 434, 793, 390, 794)));
        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));

        frontBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        frontBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));


//girl----30---back---//

        backBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 181, 385, 181, 384, 220, 304, 220)));
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 172, 300, 172)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        backBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 390, 248, 430, 203, 490, 278, 453, 328)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 149, 326, 176, 291, 235, 347, 194, 382)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_left", convertRegion(canvas, paint, 460, 347, 504, 286, 543, 327, 494, 390)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 168, 321, 205, 350, 153, 400, 110, 372)));
        backBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 486, 364, 522, 310, 579, 372, 550, 390)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 95, 359, 152, 390, 142, 418, 85, 380)));
        backBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 545, 390, 590, 359, 610, 390, 566, 420)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 259, 780, 299, 780, 299, 811, 259, 811)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 390, 786, 435, 786, 435, 813, 390, 813)));
        backBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 313, 688, 251, 687)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 440, 684, 381, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 251, 702, 313, 703, 310, 795, 250, 797)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 380, 700, 439, 690, 434, 793, 390, 794)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 268, 808, 300, 808, 300, 861, 248, 854)));
        backBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 380, 811, 424, 808, 446, 846, 387, 857)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 72, 430, 133, 420, 130, 467, 55, 472)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 565, 459, 555, 420, 625, 434, 645, 476)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 102, 400, 116, 431, 80, 490, 55, 457)));
        backBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 571, 433, 600, 395, 658, 454, 622, 482)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 257, 210, 260, 325, 205, 368, 168, 301)));
        backBodyPartObjects.add(new BodyPartObject("Arm_left", convertRegion(canvas, paint, 432, 310, 425, 207, 525, 306, 486, 374)));
        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));
    }

    //Done Verify
    public void Women55Points(Canvas canvas, Paint paint) {
//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 50, 399, 50, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 304, 75, 392, 75, 392, 95, 304, 95)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 300, 100, 315, 100, 315, 138, 300, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 377, 100, 395, 100, 395, 138, 378, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 178, 364, 178, 364, 207, 329, 207)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 145, 369, 145, 369, 159, 322, 159)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 161, 385, 161, 384, 200, 304, 200)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 330, 137, 364, 137, 364, 150, 330, 150)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 340, 104, 359, 104, 359, 136, 340, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 315, 100, 342, 100, 342, 120, 315, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 100, 382, 100, 382, 120, 356, 120)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 81, 392, 81, 392, 168, 306, 168)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 172, 292, 172)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 395, 248, 430, 203, 490, 278, 453, 318)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 225, 260, 315, 215, 350, 168, 325)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 325, 215, 350, 133, 410, 120, 385)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 435, 303, 450, 215, 522, 310, 486, 354)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 360, 522, 310, 579, 372, 550, 400)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 115, 359, 155, 390, 135, 425, 95, 395)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 555, 405, 590, 359, 610, 390, 576, 425)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 95, 400, 120, 421, 95, 480, 55, 460)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 571, 433, 600, 395, 658, 454, 622, 482)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 80, 420, 120, 451, 96, 476, 50, 472)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 592, 430, 630, 430, 657, 470, 592, 490)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 259, 340, 258, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 259, 420, 262, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 403, 371, 403, 371, 436, 320, 436)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 273, 860, 284, 860, 284, 869, 273, 869)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 404, 860, 415, 860, 415, 870, 405, 870)));


        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 259, 780, 310, 780, 310, 801, 259, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 387, 786, 435, 786, 435, 803, 387, 803)));

        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 268, 808, 305, 808, 300, 879, 248, 869)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 390, 811, 430, 808, 456, 870, 397, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 250, 854, 304, 860, 294, 881, 250, 868)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 400, 859, 464, 848, 466, 866, 404, 879)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 260, 647, 310, 646, 310, 723, 260, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 380, 648, 431, 638, 442, 730, 390, 740)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 256, 690, 323, 693, 310, 795, 266, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 690, 439, 699, 434, 793, 390, 794)));


        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 323, 683, 256, 690)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 439, 693, 390, 690)));

        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));

//back --women 55 --//

//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 172, 300, 172)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 460, 347, 504, 286, 543, 327, 494, 390)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 149, 326, 176, 291, 235, 347, 194, 382)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 542, 435, 556, 410, 630, 460, 620, 486)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 82, 437, 143, 412, 145, 432, 82, 487)));

        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 432, 310, 425, 207, 525, 306, 486, 374)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 257, 210, 260, 325, 205, 368, 168, 301)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));


        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));

        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 390, 786, 435, 786, 435, 813, 390, 813)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 259, 780, 299, 780, 299, 811, 259, 811)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 380, 700, 439, 690, 434, 793, 396, 794)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 251, 702, 313, 703, 300, 795, 250, 797)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 440, 684, 381, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 313, 688, 251, 687)));


    }

    public void Man55Points(Canvas canvas, Paint paint) {

//        frontBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 292, 50, 399, 50, 399, 84, 292, 84)));
        frontBodyPartObjects.add(new BodyPartObject("Forehead", convertRegion(canvas, paint, 304, 70, 392, 70, 392, 95, 304, 95)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Right", convertRegion(canvas, paint, 310, 100, 325, 100, 325, 138, 310, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Ears_Left", convertRegion(canvas, paint, 377, 100, 395, 100, 395, 138, 378, 136)));
        frontBodyPartObjects.add(new BodyPartObject("Throat", convertRegion(canvas, paint, 329, 178, 364, 178, 364, 200, 329, 200)));
        frontBodyPartObjects.add(new BodyPartObject("Chin", convertRegion(canvas, paint, 322, 145, 379, 145, 379, 159, 322, 159)));
        frontBodyPartObjects.add(new BodyPartObject("Neck", convertRegion(canvas, paint, 307, 161, 385, 161, 384, 200, 304, 200)));
        frontBodyPartObjects.add(new BodyPartObject("Mouth", convertRegion(canvas, paint, 330, 127, 366, 127, 366, 150, 330, 150)));
        frontBodyPartObjects.add(new BodyPartObject("Nose", convertRegion(canvas, paint, 340, 104, 359, 104, 359, 130, 340, 130)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Right", convertRegion(canvas, paint, 315, 95, 342, 95, 342, 110, 315, 110)));
        frontBodyPartObjects.add(new BodyPartObject("Eye_Left", convertRegion(canvas, paint, 356, 95, 382, 95, 382, 110, 356, 110)));
        frontBodyPartObjects.add(new BodyPartObject("Face", convertRegion(canvas, paint, 301, 75, 392, 75, 392, 168, 306, 168)));
        frontBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 172, 292, 172)));

        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Right", convertRegion(canvas, paint, 266, 203, 286, 248, 231, 324, 206, 266)));
        frontBodyPartObjects.add(new BodyPartObject("Shoulder_Left", convertRegion(canvas, paint, 395, 240, 430, 198, 490, 260, 453, 310)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 237, 225, 260, 315, 215, 350, 168, 325)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Right", convertRegion(canvas, paint, 168, 325, 215, 350, 133, 410, 120, 385)));
        frontBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 435, 303, 450, 215, 522, 310, 486, 354)));
        frontBodyPartObjects.add(new BodyPartObject("Forearm_Left", convertRegion(canvas, paint, 486, 360, 522, 310, 579, 372, 550, 400)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Right", convertRegion(canvas, paint, 115, 359, 155, 390, 135, 425, 95, 395)));
        frontBodyPartObjects.add(new BodyPartObject("Wrist_Left", convertRegion(canvas, paint, 555, 405, 590, 359, 610, 390, 576, 425)));

        frontBodyPartObjects.add(new BodyPartObject("Hand_Right", convertRegion(canvas, paint, 95, 400, 130, 421, 110, 490, 55, 470)));
        frontBodyPartObjects.add(new BodyPartObject("Hand_Left", convertRegion(canvas, paint, 571, 443, 600, 395, 658, 454, 622, 492)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 80, 420, 130, 451, 100, 490, 50, 472)));
        frontBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 572, 430, 620, 430, 637, 475, 592, 490)));

        frontBodyPartObjects.add(new BodyPartObject("Chest_Right", convertRegion(canvas, paint, 265, 259, 340, 258, 340, 341, 267, 341)));
        frontBodyPartObjects.add(new BodyPartObject("Chest_Left", convertRegion(canvas, paint, 346, 259, 420, 262, 420, 344, 351, 343)));
        frontBodyPartObjects.add(new BodyPartObject("Navel", convertRegion(canvas, paint, 320, 403, 371, 403, 371, 436, 320, 436)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Right", convertRegion(canvas, paint, 270, 258, 336, 258, 335, 378, 264, 374)));
        frontBodyPartObjects.add(new BodyPartObject("Lung_Left", convertRegion(canvas, paint, 345, 258, 409, 255, 419, 374, 355, 378)));

        frontBodyPartObjects.add(new BodyPartObject("Toenail_Right", convertRegion(canvas, paint, 250, 865, 260, 865, 260, 874, 250, 874)));
        frontBodyPartObjects.add(new BodyPartObject("Toenail_Left", convertRegion(canvas, paint, 440, 865, 450, 865, 450, 876, 440, 876)));

        frontBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 259, 780, 310, 780, 310, 801, 259, 801)));
        frontBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 397, 786, 445, 786, 445, 803, 397, 803)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Right", convertRegion(canvas, paint, 268, 808, 305, 808, 284, 860, 230, 854)));
        frontBodyPartObjects.add(new BodyPartObject("Foot_Left", convertRegion(canvas, paint, 390, 811, 445, 808, 484, 848, 420, 859)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Right", convertRegion(canvas, paint, 230, 854, 284, 860, 284, 881, 230, 878)));
        frontBodyPartObjects.add(new BodyPartObject("Toes_Left", convertRegion(canvas, paint, 420, 859, 484, 848, 486, 868, 424, 882)));

        frontBodyPartObjects.add(new BodyPartObject("Knee_Right", convertRegion(canvas, paint, 260, 647, 310, 646, 310, 723, 260, 726)));
        frontBodyPartObjects.add(new BodyPartObject("Knee_Left", convertRegion(canvas, paint, 380, 648, 431, 638, 442, 730, 390, 740)));

        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 256, 690, 323, 693, 310, 795, 266, 797)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 390, 690, 439, 695, 440, 793, 400, 794)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 252, 516, 331, 533, 323, 683, 256, 690)));
        frontBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 355, 538, 440, 514, 439, 693, 390, 690)));

        frontBodyPartObjects.add(new BodyPartObject("Abdomen", convertRegion(canvas, paint, 248, 470, 440, 470, 440, 498, 248, 498)));
        frontBodyPartObjects.add(new BodyPartObject("Waist", convertRegion(canvas, paint, 258, 403, 431, 403, 431, 486, 258, 486)));
        frontBodyPartObjects.add(new BodyPartObject("Heart", convertRegion(canvas, paint, 338, 269, 377, 272, 377, 303, 338, 302)));

//Back
//        backBodyPartObjects.add(new BodyPartObject("Hair", convertRegion(canvas, paint, 300, 56, 394, 53, 396, 172, 300, 172)));
        backBodyPartObjects.add(new BodyPartObject("Head", convertRegion(canvas, paint, 292, 51, 397, 51, 397, 192, 292, 192)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Right", convertRegion(canvas, paint, 460, 347, 504, 266, 543, 307, 494, 390)));
        backBodyPartObjects.add(new BodyPartObject("Elbow_Left", convertRegion(canvas, paint, 142, 315, 176, 281, 235, 367, 190, 382)));

        backBodyPartObjects.add(new BodyPartObject("Fingers_Right", convertRegion(canvas, paint, 542, 435, 556, 410, 650, 470, 630, 496)));
        backBodyPartObjects.add(new BodyPartObject("Fingers_Left", convertRegion(canvas, paint, 62, 437, 143, 412, 145, 436, 62, 490)));

        backBodyPartObjects.add(new BodyPartObject("Arm_Right", convertRegion(canvas, paint, 432, 280, 425, 180, 525, 280, 486, 344)));
        backBodyPartObjects.add(new BodyPartObject("Arm_Left", convertRegion(canvas, paint, 257, 190, 250, 305, 195, 348, 168, 290)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Lumbar", convertRegion(canvas, paint, 329, 356, 366, 356, 366, 452, 329, 452)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Cervical", convertRegion(canvas, paint, 329, 163, 366, 163, 366, 226, 329, 226)));
        backBodyPartObjects.add(new BodyPartObject("Spine_Thoracic", convertRegion(canvas, paint, 329, 226, 366, 226, 366, 356, 329, 356)));

        backBodyPartObjects.add(new BodyPartObject("Back", convertRegion(canvas, paint, 267, 189, 424, 186, 441, 462, 248, 462)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Right", convertRegion(canvas, paint, 409, 473, 439, 473, 444, 541, 414, 541)));
        backBodyPartObjects.add(new BodyPartObject("Hip_Left", convertRegion(canvas, paint, 253, 473, 283, 473, 283, 541, 253, 541)));
        backBodyPartObjects.add(new BodyPartObject("Buttock", convertRegion(canvas, paint, 253, 478, 439, 478, 444, 532, 253, 532)));

        backBodyPartObjects.add(new BodyPartObject("Ankle_Right", convertRegion(canvas, paint, 390, 786, 455, 786, 455, 813, 390, 813)));
        backBodyPartObjects.add(new BodyPartObject("Ankle_Left", convertRegion(canvas, paint, 249, 780, 299, 780, 299, 811, 249, 811)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Right", convertRegion(canvas, paint, 380, 700, 449, 690, 454, 793, 396, 794)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Lower_Left", convertRegion(canvas, paint, 251, 702, 313, 703, 300, 795, 250, 797)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Right", convertRegion(canvas, paint, 355, 538, 440, 514, 440, 684, 381, 690)));
        backBodyPartObjects.add(new BodyPartObject("Leg_Upper_Left", convertRegion(canvas, paint, 252, 516, 331, 533, 313, 688, 251, 687)));
    }


    public class QuestionWithMatchcount {
        Double wordMatchCout;
        Definition definition;
    }
}