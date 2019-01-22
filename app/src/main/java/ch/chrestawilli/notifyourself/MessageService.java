package ch.chrestawilli.notifyourself;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageService extends FirebaseMessagingService  {
    private NotificationManagerCompat notificationManager;
    private MessageStore messageStore;
    private LocalBroadcastManager localBroadcastManager;

    public static AtomicInteger notificationId = new AtomicInteger(0);

    public static final String ACTION_NEW_MESSAGE = "ch.chrestawilli.notifyourself.NEW_MESSAGE";
    public static final String ACTION_TOKEN_CHANGE = "ch.chrestawilli.notifyourself.TOKEN_CHANGE";

    public static final String MESSAGE_TITLE = "title";
    public static final String MESSAGE_BODY = "body";
    public static final String MESSAGE_TIMESTAMP = "timestamp";

    public NotificationChannel notificationChannel;
    public static final String NOTIFICATION_CHANNEL = "ch.chrestawilli.notifyourself.NOTIFICATION";
    
    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("MessageService", "Service is up...");

        messageStore = new MessageStore(getFilesDir());
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("default", NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public void onDestroy() {
        // Save messages to store
        Log.d("MessageService", "Service destroyed...");
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload
        Map<String, String> data = remoteMessage.getData();
        if (data.size() >= 3) {
            String title = data.get(MESSAGE_TITLE);
            String body = data.get(MESSAGE_BODY);
            long timestamp = Long.valueOf(data.get(MESSAGE_TIMESTAMP));

            Message message = new Message(title, body, timestamp);
            storeMessage(message);
            broadcastNewMessage(message);

            showNotification(message);
        }
    }

    private void showNotification(Message message) {
        // Create an explicit intent for an Activity in your app
        int nextNotificationId = notificationId.getAndAdd(1);

        Intent intent = new Intent(MainNavigationActivity.ACTION_SHOW_MESSAGES);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notificationId", nextNotificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "default")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(message.getTitle())
                        .setContentText(message.getBody())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(nextNotificationId, notificationBuilder.build());
    }

    private void storeMessage(Message message) {
        messageStore.addMessage(message);
    }

    private void broadcastNewMessage(Message message) {
        Intent intent = new Intent(ACTION_NEW_MESSAGE);
        intent.putExtra("message", (Serializable) message);

        Log.d("MessageService", "Broadcasting Message");

        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        broadcastTokenChange(token);
    }

    private void broadcastTokenChange(String token) {
        Intent intent = new Intent(ACTION_TOKEN_CHANGE);
        intent.putExtra("token", token);

        Log.d("MessageService", "Broadcasting Token Change");
        localBroadcastManager.sendBroadcast(intent);
    }
}
