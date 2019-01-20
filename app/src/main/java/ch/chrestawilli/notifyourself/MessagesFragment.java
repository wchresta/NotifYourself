package ch.chrestawilli.notifyourself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private RecyclerView messagesView;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String title = intent.getExtras().getString(MessagingService.EXTRA_TITLE);
                String text = intent.getExtras().getString(MessagingService.EXTRA_TEXT);
                String label = intent.getExtras().getString(MessagingService.EXTRA_LABEL);

                messageList.add(new Message(title, text, label));
                messageAdapter.notifyDataSetChanged();
            } catch (NullPointerException e) {
                Log.e("MessageOnReceive", "getString produced NullPointerException (BUG)");
                return;
            }
        }
    };


    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up message view
        messageAdapter = new MessageAdapter(messageList);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        RecyclerView.LayoutManager messageLayoutManager = new LinearLayoutManager(view.getContext());

        messagesView = view.findViewById(R.id.messagesView);
        messagesView.setLayoutManager(messageLayoutManager);
        messagesView.setItemAnimator(new DefaultItemAnimator());
        messagesView.setAdapter(messageAdapter);

        // Receive Messages using Broadcasts
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((messageReceiver),
                new IntentFilter("NotifYourselfMessage")
        );

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
    }
}
