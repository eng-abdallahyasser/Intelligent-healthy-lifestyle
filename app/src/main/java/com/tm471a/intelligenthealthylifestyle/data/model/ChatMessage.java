package com.tm471a.intelligenthealthylifestyle.data.model;

public class ChatMessage {
    private String content;
    private boolean isBot;
    private boolean isTyped=false;
    private long timestamp;

    public ChatMessage(String content, boolean isBot) {
        this.content = content;
        this.isBot = isBot;
        this.timestamp = System.currentTimeMillis();
    }

    public ChatMessage() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBot() { return isBot; }
    public String getContent() { return content; }

    public boolean isTyped() {
        return isTyped;
    }

    public void setTyped(boolean typed) {
        isTyped = typed;
    }
}