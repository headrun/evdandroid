    package com.headrun.evidyaloka.activity.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.activity.base.MainActivity;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.model.SessionDetails;
import com.headrun.evidyaloka.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sujith on 10/2/17.
 */
public class EVDFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = EVDFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification notify = remoteMessage.getNotification();


            Map<String, String> data = remoteMessage.getData();

            Log.i(TAG, "Message Notification and data : " + notify + " " + data);
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            new GetUserData(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());

        } else {
            Log.i(TAG, "Message Notification remoteMessage.getNotification() getting null");
        }

    }

    public void sendNotification(String title, String messageBody, Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
                Log.i(TAG, "notify key is " + entry.getKey() + "get value is " + entry.getValue());
            }
        } else {
            Log.i(TAG, "notify data is null " + data);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.evidyalogo);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


      String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.evidyalogo)
                .setContentTitle(title)
                .setLargeIcon(bitmap)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{Notification.DEFAULT_SOUND})
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.PRIORITY_MAX)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      // Since android Oreo notification channel is needed.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(channelId,
            "EVD",
            NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
      }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public class GetUserData implements ResponseListener<LoginResponse> {
        String title, messagebody;
        Context mContext;
        Map<String, String> data;

        public GetUserData(Context mContext, String title, String messagebody, Map<String, String> data) {
            this.title = title;
            this.messagebody = messagebody;
            this.mContext = mContext;
            this.data = data;
            new EVDNetowrkServices().getUserData(mContext, this);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            sendNotification(title, messagebody, data);
        }

        @Override
        public void onResponse(LoginResponse response) {

            if (response.status == 0) {
                new Utils(mContext).userSession.setUserData(response);
                sendNotification(title, messagebody, data);
            }
        }
    }
}

