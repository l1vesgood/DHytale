package dev.hytalemodding;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import dev.hytalemodding.commands.ExampleCommand;
import dev.hytalemodding.events.ExampleEvent;
import dev.hytalemodding.events.ChatListener;
import dev.hytalemodding.discord.DiscordManager;

import javax.annotation.Nonnull;

public class DHytale extends JavaPlugin {

    private DiscordManager discordManager;
    // TODO: Replace with your actual Bot Token and Channel ID
    private static final String DISCORD_TOKEN = "YOUR_DISCORD_TOKEN";
    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";

    public DHytale(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Initialize Discord Manager
        this.discordManager = new DiscordManager(DISCORD_TOKEN, CHANNEL_ID, this::broadcastToGame);
        this.discordManager.start();

        // Register existing commands and events
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

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
