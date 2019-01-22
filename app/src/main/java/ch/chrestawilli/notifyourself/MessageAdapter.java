package ch.chrestawilli.notifyourself;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<Message> messageList;
    private final DateFormat dateFormat;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body, timestamp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }

    MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
        this.dateFormat = DateFormat.getDateTimeInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Message message = messageList.get(position);
        String title = message.getTitle();
        if (title != null) {
            viewHolder.title.setText(title);
        } else {
            viewHolder.title.setText("-");
        }
        viewHolder.body.setText(message.getBody());
        viewHolder.timestamp.setText(dateFormat.format(message.getDate()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
