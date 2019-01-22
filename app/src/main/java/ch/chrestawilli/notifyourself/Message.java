package ch.chrestawilli.notifyourself;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String title;
    private String body;
    private long timestamp;

    Message(@Nullable String title, String body, long timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    Message(@Nullable String title, String body) {
        this.title = title;
        this.body = body;
        this.timestamp = System.currentTimeMillis();
    }

    @Nullable
    String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
