package com.livesgood.config;

import java.util.Map;

public class BotConfig {
    private String botToken;
    private String channelId;
    private String language;
    private Map<String, MessagesConfig> messages;

    public BotConfig(String botToken, String channelId, String language, Map<String, MessagesConfig> messages) {
        this.botToken = botToken;
        this.channelId = channelId;
        this.language = language;
        this.messages = messages;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, MessagesConfig> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, MessagesConfig> messages) {
        this.messages = messages;
    }

    public MessagesConfig getActiveMessages() {
        if (messages != null && messages.containsKey(language)) {
            return messages.get(language);
        }

        if (messages != null && messages.containsKey("en")) {
            return messages.get("en");
        }
        return null;
    }
}
