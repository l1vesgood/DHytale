package com.livesgood.config;

public class BotConfig {
    private String botToken;
    private String channelId;

    public BotConfig(String botToken, String channelId) {
        this.botToken = botToken;
        this.channelId = channelId;
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
}
