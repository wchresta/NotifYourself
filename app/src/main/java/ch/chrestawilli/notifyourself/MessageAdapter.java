package ch.chrestawilli.notifyourself;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<Message> messageList;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
        }
    }

    MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
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
        viewHolder.text.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
