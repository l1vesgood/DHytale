package com.livesgood.config;

public class MessagesConfig {
    private String discordJoinTitle;
    private String discordLeaveTitle;

    public MessagesConfig(String discordJoinTitle, String discordLeaveTitle) {
        this.discordJoinTitle = discordJoinTitle;
        this.discordLeaveTitle = discordLeaveTitle;
    }

    public String getDiscordJoinTitle() {
        return discordJoinTitle;
    }

    public String getDiscordLeaveTitle() {
        return discordLeaveTitle;
    }

}
