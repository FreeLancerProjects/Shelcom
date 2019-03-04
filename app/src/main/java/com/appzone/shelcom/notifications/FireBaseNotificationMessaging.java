package com.appzone.shelcom.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.NotificationModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;
import java.util.Map;

public class FireBaseNotificationMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        for (String key : remoteMessage.getData().keySet()) {
            Log.e("Key", key + " " + "value" + " " + remoteMessage.getData().get(key));
        }

        manageNotification(map);


    }

    private void manageNotification(final Map<String, String> map) {
        String session = getSession();
        UserModel userModel = getUserData();
        String to_id = map.get("to_id");
        if (session.equals(Tags.session_login)) {
            if (userModel != null && userModel.getData().getUser_id().equals(to_id)) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateNotification(map);
                            }
                        }, 1);

            }
        }
    }

    private void CreateNotification(Map<String, String> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateProfessionalNotification(map);
        } else {
            CreateNativeNotification(map);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateProfessionalNotification(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String status = map.get("status");
        String ar_title = map.get("ar_title");
        String en_title = map.get("en_title");
        String created_at = map.get("creation_date");
        String to_user = map.get("to_user");

        NotificationModel notificationModel = new NotificationModel(ar_title,en_title,status,to_user,created_at);

        String CHANNEL_ID = "my_channel_01";
        CharSequence CHANNEL_NAME = "channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path));
        builder.setContentTitle(getString(R.string.admin));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(bitmap);
        builder.setColor(ContextCompat.getColor(this,R.color.colorPrimary));
        builder.setColorized(true);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",status);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (status.equals(Tags.NOTIFICATION_ACCEPT_SERVICE))
        {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getAr_title());


            }else
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getEn_title());


            }
        }
        else if (status.equals(Tags.NOTIFICATION_REFUSE_SERVICE))
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getAr_title());

            }else
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getEn_title());


            }
        }
        else if (status.equals(Tags.NOTIFICATION_FINISH_SERVICE))
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {

                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getAr_title());

            }else
            {
                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getEn_title());


            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(1, builder.build());
            EventBus.getDefault().post(notificationModel);
        }


    }

    private void CreateNativeNotification(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String status = map.get("status");
        String ar_title = map.get("ar_title");
        String en_title = map.get("en_title");
        String created_at = map.get("creation_date");
        String to_user = map.get("to_user");

        NotificationModel notificationModel = new NotificationModel(ar_title,en_title,status,to_user,created_at);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path));
        builder.setContentTitle(getString(R.string.admin));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(bitmap);
        builder.setColor(ContextCompat.getColor(this,R.color.colorPrimary));
        builder.setColorized(true);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",status);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (status.equals(Tags.NOTIFICATION_ACCEPT_SERVICE))
        {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getAr_title());


            }else
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getEn_title());


            }
        }
        else if (status.equals(Tags.NOTIFICATION_REFUSE_SERVICE))
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getAr_title());

            }else
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getEn_title());


            }
        }
        else if (status.equals(Tags.NOTIFICATION_FINISH_SERVICE))
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {

                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getAr_title());

            }else
            {
                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getEn_title());


            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
            EventBus.getDefault().post(notificationModel);
        }


    }


    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }
}
