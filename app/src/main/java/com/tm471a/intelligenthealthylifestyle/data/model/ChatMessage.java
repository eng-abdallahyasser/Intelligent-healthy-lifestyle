package com.tm471a.intelligenthealthylifestyle.data.model;

public class ChatMessage {
    private String content;
    private boolean isBot;

    public ChatMessage(String content, boolean isBot) {
        this.content = content;
        this.isBot = isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBot() { return isBot; }
    public String getContent() { return content; }
}