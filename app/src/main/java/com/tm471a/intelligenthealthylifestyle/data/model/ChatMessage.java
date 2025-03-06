package com.tm471a.intelligenthealthylifestyle.data.model;

public class ChatMessage {
    private String content;
    private boolean isBot;
    private boolean isLoading;

    public ChatMessage(String content, boolean isBot) {
        this.content = content;
        this.isBot = isBot;
        this.isLoading = false;
    }

    public static ChatMessage createLoading() {
        ChatMessage message = new ChatMessage("", false);
        message.setLoading(true);
        return message;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLoading() { return isLoading; }
    public void setLoading(boolean loading) { isLoading = loading; }
    public boolean isBot() { return isBot; }
    public String getContent() { return content; }
}