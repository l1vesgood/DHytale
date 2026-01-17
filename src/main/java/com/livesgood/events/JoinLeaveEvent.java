package com.livesgood.events;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.livesgood.discord.DiscordManager;
import com.livesgood.config.BotConfig;
import com.livesgood.config.MessagesConfig;
import java.awt.Color;

public class JoinLeaveEvent {

    private final DiscordManager discordManager;
    private final BotConfig config;

    public JoinLeaveEvent(DiscordManager discordManager, BotConfig config) {
        this.discordManager = discordManager;
        this.config = config;
    }

    public void onPlayerReady(PlayerReadyEvent event) {
        MessagesConfig msg = config.getActiveMessages();
        String username = event.getPlayer().getDisplayName();

        if (msg != null) {
            String title = msg.getDiscordJoinTitle().replace("{player}", username);

            discordManager.sendEmbed(title, Color.GREEN);
        }
    }

    public void onPlayerLeave(PlayerDisconnectEvent event) {
        String username = event.getPlayerRef().getUsername();
        MessagesConfig msg = config.getActiveMessages();

        if (msg != null) {
            String title = msg.getDiscordLeaveTitle().replace("{player}", username);

            discordManager.sendEmbed(title, Color.RED);
        }
    }
}
