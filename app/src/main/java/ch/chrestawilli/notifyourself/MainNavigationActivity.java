package ch.chrestawilli.notifyourself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.LinkedList;

public class MainNavigationActivity extends AppCompatActivity {
    public static final String ACTION_SHOW_MESSAGES = "ch.chrestawilli.notifyourself.ACTION_SHOW_MESSAGES";

    private String secretToken;
    private Task<InstanceIdResult> secretTokenGetter;

    private LocalBroadcastManager localBroadcastManager;
    private MessageStore messageStore;
    private LinkedList<Message> messageList;

    private Fragment currentFragment;

    NotificationManagerCompat notificationManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navifation_messages:
                    currentFragment = MessagesFragment.newInstance(messageList);
                    break;
                case R.id.navigation_settings:
                    if (secretToken != null) {
                        currentFragment = SettingsFragment.newInstance(secretToken);
                    } else {
                        currentFragment = SettingsFragment.newInstance(secretTokenGetter);
                    }
                    break;
                default:
                    return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, currentFragment)
                    .commit();

            return true;
        }
    };


    // Be able to receive Messages
    private BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MessageService.ACTION_NEW_MESSAGE:
                    Log.d("MessageFragment", "Got message");

                    messageList.addFirst((Message) intent.getSerializableExtra("message"));
                    notifyDataSetChanged();
                    break;

                /*
                case MainNavigationActivity.ACTION_SHOW_MESSAGES:
                    // User clicked on a notification
                    int notificationId = intent.getIntExtra("notificationId", -1);
                    if (notificationId >= 0) {
                        //notificationManager.cancel(notificationId);
                    }
                    break;
                */
            }
        }
    };

    private void showMessageFragment() {
        if (! MessagesFragment.class.isInstance(currentFragment)) {
            // Activate Messages Fragment
            currentFragment = MessagesFragment.newInstance(messageList);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, currentFragment)
                .commit();
    }

    private void notifyDataSetChanged() {
        if (MessagesFragment.class.isInstance(currentFragment)) {
            ((MessagesFragment) currentFragment).notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, getString(R.string.secretsAdmobAppId));


        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // Retrieve Firebase Token
        secretTokenGetter = FirebaseInstanceId.getInstance().getInstanceId();
        secretTokenGetter.addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                secretToken = instanceIdResult.getToken();
                secretTokenGetter = null;
                Log.i("Secret Token", secretToken);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        notificationManager = NotificationManagerCompat.from(this);

        // Load messages
        messageStore = new MessageStore(getFilesDir());

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageList = messageStore.load();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageService.ACTION_NEW_MESSAGE);
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter);

        showMessageFragment();
    }

    @Override
    // OnStop will be called even when we force shutdown
    protected void onStop() {
        localBroadcastManager.unregisterReceiver(localBroadcastReceiver);
        messageStore.store(messageList);
        super.onStop();
    }
}
