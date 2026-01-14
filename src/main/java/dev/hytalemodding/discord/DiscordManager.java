package dev.hytalemodding.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class DiscordManager extends ListenerAdapter {

    private JDA jda;
    private final String token;
    private final String channelId;
    private final BiConsumer<String, String> onMessageCallback; // User, Message

    public DiscordManager(String token, String channelId, BiConsumer<String, String> onMessageCallback) {
        this.token = token;
        this.channelId = channelId;
        this.onMessageCallback = onMessageCallback;
    }

    public void start() {
        try {
            this.jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(this)
                    .build();
            this.jda.awaitReady();
            System.out.println("Discord Bot started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (this.jda != null) {
            this.jda.shutdown();
        }
    }

    public void sendMessage(String username, String content) {
        if (this.jda == null) return;
        var channel = this.jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage("**" + username + "**: " + content).queue();
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().getId().equals(channelId)) return;

        String user = event.getAuthor().getName();
        String message = event.getMessage().getContentDisplay();

        if (onMessageCallback != null) {
            onMessageCallback.accept(user, message);
        }
    }
}
