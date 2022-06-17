package com.ar.patient.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.ar.patient.R;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Random;

import static androidx.core.app.NotificationCompat.BigTextStyle;
import static androidx.core.app.NotificationCompat.Builder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String PRIMARY_CHANNEL = "default";
    public static NotificationManager notificationManager = null;
    private static int MY_NOTIFICATION_ID = 1;
    public RemoteMessage remoteMessage;
    public String action;
    Notification myNotification;
    private String message, type, room_id, room_name;
    private String title;
    private NotificationChannel mChannel;
    private Builder builder;

    /*Date today = new Date(System.currentTimeMillis());
           SimpleDateFormat   f = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
           String str = f.format(today);*/

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final int min = 20;
        final int max = 80;
        MY_NOTIFICATION_ID = new Random().nextInt((max - min) + 1) + min;
        Log.e("checkNotification", "" + remoteMessage.getData().toString());

        if (Pref.getValue(MyFirebaseMessagingService.this, Config.PREF_USERID, 0) != 0) {
            this.remoteMessage = remoteMessage;
            Log.e("notification data", remoteMessage.getData().toString());
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");

            boolean isAppRuning = isAppIsInBackground(this);
            Log.e("app is background", String.valueOf(isAppRuning));

            try {
                type = remoteMessage.getData().get("type");
                if (type == null)
                    type = "";
            } catch (Exception e) {
                e.printStackTrace();
                type.equals("");
            }


        }
    }

    public PendingIntent getPendingIntent(Intent intent) {
        return PendingIntent.getActivity(this, MY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void showOnlyNotification(String title, PendingIntent pendingIntent) {

        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance;

            importance = NotificationManager.IMPORTANCE_HIGH;

            mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(message);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new Builder(this, id);
            builder.setContentTitle(title)  // required
                    .setSmallIcon(R.mipmap.ic_launcher) // required
                    .setContentText(message)  // required
                    .setStyle(new BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(pendingIntent)
                    .setTicker(title)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new Builder(this);
            builder.setContentTitle(title)
                    .setContentText(message)// required
                    .setStyle(new BigTextStyle().bigText(message))
                    .setSmallIcon(R.mipmap.ic_launcher) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(title)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder.setColor(getResources().getColor(R.color.colorPrimary));
        }

        Notification notification = builder.build();
        notificationManager.notify(MY_NOTIFICATION_ID, notification);
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
