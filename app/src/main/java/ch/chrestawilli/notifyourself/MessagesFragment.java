package ch.chrestawilli.notifyourself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MessagesFragment extends Fragment {
    private RecyclerView messagesView;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance(List<Message> messageList) {
        MessagesFragment messagesFragment = new MessagesFragment();
        messagesFragment.setMessageList(messageList);
        return messagesFragment;
    }

    private void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        RecyclerView.LayoutManager messageLayoutManager =
                new LinearLayoutManager(view.getContext());

        messagesView = view.findViewById(R.id.messagesView);
        messagesView.setLayoutManager(messageLayoutManager);
        messagesView.setItemAnimator(new DefaultItemAnimator());

        messageAdapter = new MessageAdapter(messageList);
        messagesView.setAdapter(messageAdapter);

        return view;
    }

    public void notifyDataSetChanged() {
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
