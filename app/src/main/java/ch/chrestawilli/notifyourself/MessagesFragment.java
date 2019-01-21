package ch.chrestawilli.notifyourself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MessagesFragment extends Fragment {
    private RecyclerView messagesView;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private MessageStore messageStore;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String title = intent.getExtras().getString(MessagingService.EXTRA_TITLE);
                String text = intent.getExtras().getString(MessagingService.EXTRA_TEXT);
                String label = intent.getExtras().getString(MessagingService.EXTRA_LABEL);
                long timestamp = System.currentTimeMillis();

                messageList.add(new Message(title, text, label, timestamp));
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

    public static Fragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set up message view
        messageStore = new MessageStore(getActivity().getFilesDir());
        messageList = messageStore.load();
        messageAdapter = new MessageAdapter(messageList);

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
        messageStore.store(messageList); // Save messages
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
    }
}
