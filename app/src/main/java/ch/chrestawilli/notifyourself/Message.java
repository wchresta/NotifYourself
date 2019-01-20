package ch.chrestawilli.notifyourself;

import android.support.annotation.Nullable;

public class Message {
    private String title;
    private String text;
    private String label;

    Message(@Nullable String title, String text, @Nullable String label) {
        this.title = title;
        this.text = text;
        this.label = label;
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
}
