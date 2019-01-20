package ch.chrestawilli.notifyourself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainNavigationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final Fragment selectedFragment;

            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    selectedFragment = MessagesFragment.newInstance();
                    break;
                case R.id.navigation_token:
                    if (secretToken != null) {
                        selectedFragment = SettingsFragment.newInstance(secretToken);
                    } else {
                        selectedFragment = SettingsFragment.newInstance(secretTokenGetter);
                    }
                    break;
                default:
                    return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment)
                    .commit();

            return true;
        }
    };

    private String secretToken;
    private Task<InstanceIdResult> secretTokenGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_notifications);
    }
}
