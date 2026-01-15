package com.livesgood.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.livesgood.discord.DiscordManager;

public class ChatListener {

    private final DiscordManager discordManager;

    public ChatListener(DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    public void onPlayerChat(PlayerChatEvent event) {
        String username = event.getSender().getUsername();
        String message = event.getContent();
        discordManager.sendMessage(username, message);
    }
}
