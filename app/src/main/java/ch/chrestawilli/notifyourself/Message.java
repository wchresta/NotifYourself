package ch.chrestawilli.notifyourself;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String title;
    private String text;
    private String label;
    private long timestamp;

    Message(@Nullable String title, String text, @Nullable String label, long timestamp) {
        this.title = title;
        this.text = text;
        this.label = label;
        this.timestamp = timestamp;
    }

    Message(@Nullable String title, String text, @Nullable String label) {
        this.title = title;
        this.text = text;
        this.label = label;
        this.timestamp = System.currentTimeMillis();
    }

    @Nullable
    String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getDate() {
        return new Date(timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
