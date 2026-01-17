package com.livesgood;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.livesgood.commands.ExampleCommand;
import com.livesgood.events.ChatListener;
import com.livesgood.discord.DiscordManager;

import javax.annotation.Nonnull;

import com.livesgood.config.ConfigManager;
import com.livesgood.config.BotConfig;
import com.livesgood.events.JoinLeaveEvent;

import java.awt.*;

public class HDiscordSRV extends JavaPlugin {

    private DiscordManager discordManager;
    private ConfigManager configManager;
    private final String ModName = HDiscordSRV.class
            .getPackage()
            .getName();
    Color dcolor = new Color(100, 112, 255);

    public HDiscordSRV(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.configManager = new ConfigManager(this.getDataDirectory());
        this.configManager.load();
        
        BotConfig config = this.configManager.getConfig();
        String token = config.getBotToken();
        String channelId = config.getChannelId();

        if ("YOUR_BOT_TOKEN".equals(token) || "YOUR_CHANNEL_ID".equals(channelId)) {
            System.err.println("[" + ModName + "] Please configure your bot token and channel ID in " + this.getDataDirectory().resolve("config.json"));
            return;
        }

        // Initialize Discord Manager
        this.discordManager = new DiscordManager(token, channelId, this::broadcastToGame);
        this.discordManager.start();

        // Initialize Events
        JoinLeaveEvent joinLeaveEvent = new JoinLeaveEvent(discordManager, config);

        // Register existing commands and events
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, joinLeaveEvent::onPlayerReady);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, joinLeaveEvent::onPlayerLeave);

        // Register Chat Listener for Discord <-> Hytale communication
        ChatListener chatListener = new ChatListener(discordManager);
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, chatListener::onPlayerChat);
    }

    @Override
    protected void shutdown() {
        if (discordManager != null) {
            discordManager.stop();
        }
    }

    private void broadcastToGame(String user, String message) {
        // Broadcast message to all connected players
        try {
            Universe.get().sendMessage(Message.join(
                    Message.raw("[Discord] ").color(dcolor),
                    Message.raw(user + ": ").color(Color.WHITE),
                    Message.raw(message).color(Color.GRAY) // ex. new Color(128, 128, 128);
                    ));
        } catch (Exception e) {
            System.err.println("Error broadcasting to Hytale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
