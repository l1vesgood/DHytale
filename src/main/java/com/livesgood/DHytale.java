package com.livesgood;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.livesgood.commands.ExampleCommand;
import com.livesgood.events.ChatListener;
import com.livesgood.discord.DiscordManager;

import javax.annotation.Nonnull;

import com.livesgood.config.ConfigManager;
import com.livesgood.config.BotConfig;

public class DHytale extends JavaPlugin {

    private DiscordManager discordManager;
    private ConfigManager configManager;
    private final String ModName = DHytale.class
            .getPackage()
            .getName();

    public DHytale(@Nonnull JavaPluginInit init) {
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

        // Register existing commands and events
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
//        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

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
            Universe.get().sendMessage(Message.raw("[Discord] " + user + ": " + message));
        } catch (Exception e) {
            System.err.println("Error broadcasting to Hytale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
