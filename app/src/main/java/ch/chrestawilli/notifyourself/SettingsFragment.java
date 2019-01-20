package ch.chrestawilli.notifyourself;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private String secretToken;

    private TextView tokenText;
    private ImageButton tokenCopyButton;

    private View.OnClickListener tokenCopyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (secretToken != null) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("notifYourself Security Token", secretToken));

                Snackbar.make(view, "Security Token copied", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String secretToken) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.secretToken = secretToken;
        return fragment;
    }

    public static SettingsFragment newInstance(Task<InstanceIdResult> secretTokenGeter) {
        final SettingsFragment fragment = new SettingsFragment();
        secretTokenGeter.addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                fragment.secretToken = instanceIdResult.getToken();
                fragment.showToken();
            }
        });
        return fragment;
    }

    private void showToken() {
        if (secretToken != null) {
            tokenText.setText(secretToken);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tokenText = view.findViewById(R.id.tokenText);

        tokenCopyButton = view.findViewById(R.id.tokenCopyButton);
        tokenCopyButton.setOnClickListener(tokenCopyButtonListener);

        showToken();
        return view;
    }
}
