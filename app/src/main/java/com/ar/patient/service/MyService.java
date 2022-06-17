package com.ar.patient.service;

import android.Manifest.permission;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.ar.patient.R;
import com.ar.patient.activity.PatientExamActivity;
import com.ar.patient.fcm.MessageEvent;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyService extends Service implements SpeechDelegate, Speech.stopDueToDelay {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static SpeechDelegate delegate;
    public int pause;
    public boolean isMatch = false, isSpeaking = false;
    private String speakingText;

    private void createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setSound(null, null);
            serviceChannel.setShowBadge(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Log.d("mytag", "::::::MyService:onStartCommand::");

        speakingText = intent.getStringExtra("text");
        Log.d("mytag", "Speaking text : " + speakingText);
        Pref.setValue(this, Config.PREF_WAKE_UP_COMMAND, speakingText);
//        try {
//            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//                ((AudioManager) Objects.requireNonNull(
//                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        setinit();

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, PatientExamActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Listening Service")
                .setSound(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        return START_NOT_STICKY;
    }

    public void setinit() {

        Speech.init(this);
        delegate = this;
        Speech.getInstance().setListener(this);

        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
            muteBeepSoundOfRecorder("isListening");
        } else {
            System.setProperty("rx.unsafe-disable", "True");
            RxPermissions.getInstance(this).request(permission.RECORD_AUDIO).subscribe(granted -> {
                if (granted) { // Always true pre-M
                    try {
                        Speech.getInstance().stopTextToSpeech();
                        Speech.getInstance().startListening(null, this);
                    } catch (SpeechRecognitionNotAvailable exc) {
                        //showSpeechNotSupportedDialog();

                    } catch (GoogleVoiceTypingDisabledException exc) {
                        //showEnableGoogleVoiceTyping();
                    }
                } else {
                    Toast.makeText(this, R.string.permission_listener_null_exception_text, Toast.LENGTH_LONG).show();
                }
            });
            muteBeepSoundOfRecorder("isListening NOT");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        for (String partial : results) {
            Log.d("Result", partial + "");
        }
    }

    @Override
    public void onSpeechResult(String result) {
        if (!isSpeaking) {
            if (!TextUtils.isEmpty(result)) {
                isMatch = true;
                EventBus.getDefault().post(new MessageEvent(result.toLowerCase()));

            } else {
                muteBeepSoundOfRecorder("Result Blank");
            }
        }
    }

    @Override
    public void onSpecifiedCommandPronounced(String event) {
//        Log.d("mytag", "::onSpecifiedCommandPronounced:");

        if (Speech.getInstance().isListening()) {
            Log.d("mytag", "::stopListening ::onSpecifiedCommandPronounced:");
            Speech.getInstance().stopListening();
        } else {
            System.out.println("::startListening ::onSpecifiedCommandPronounced:");
            RxPermissions.getInstance(this).request(permission.RECORD_AUDIO).subscribe(granted -> {
                if (granted) { // Always true pre-M
                    try {
                        Speech.getInstance().stopTextToSpeech();
                        Speech.getInstance().startListening(null, this);
                    } catch (SpeechRecognitionNotAvailable exc) {

                    } catch (GoogleVoiceTypingDisabledException exc) {
                    }
                } else {
                    Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Function to remove the beep sound of voice recognizer.
     */
    private void muteBeepSoundOfRecorder(String from) {
        if (Config.isMute) {
            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (amanager != null) {
                Log.d("mytag", ":::::muteBeepSoundOfRecorder:::::" + from);
                if (Pref.getValue(MyService.this, Config.PREF_SILENT_CALL, 0) == 1) {
                    amanager.setStreamVolume(AudioManager.STREAM_RING, 1, 0);

                }
                amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            }
        }
    }

    private void unmuteBeepSoundOfRecorder(String from) {
        if (Config.isMute) {
            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (amanager != null) {
                Log.d("mytag", ":::::unmuteBeepSoundOfRecorder:::::" + from);
                amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("mytag", "::::::MyService:onTaskRemoved::");
        //Restarting the service if it is removed.
        PendingIntent service =
                PendingIntent.getService(getApplicationContext(), new Random().nextInt(),
                        new Intent(getApplicationContext(), MyService.class), PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("mytag", "::::::MyService:onDestroy::");
        try {
            Speech.getInstance().stopListening();
        } catch (Exception e) {
            Log.d("mytag", ":::::::" + e.getLocalizedMessage());
        }
        unmuteBeepSoundOfRecorder("onDestroy");
    }
}