package ch.chrestawilli.notifyourself;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    final static String INTENT_ACTION = "NotifYourselfMessage";
    final static String EXTRA_TITLE = "title";
    final static String EXTRA_TEXT = "text";
    final static String EXTRA_LABEL = "topic";

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(EXTRA_TITLE, notification.getTitle());
            intent.putExtra(EXTRA_TEXT, notification.getBody());
            intent.putExtra(EXTRA_LABEL, notification.getTag());

            broadcaster.sendBroadcast(intent);
        }
    }
}
