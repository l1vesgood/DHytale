package com.livesgood.discord;

import com.livesgood.HDiscordSRV;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;

public class DiscordManager extends ListenerAdapter {

    private JDA jda;
    private final String token;
    private final String channelId;
    private final BiConsumer<String, String> onMessageCallback; // User, Message
    private final String ModName = HDiscordSRV.class
            .getPackage()
            .getName();

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
            channel.retrieveWebhooks().queue(webhooks -> {
                Webhook webhook = webhooks.stream()
                        .filter(w -> w.getName().equalsIgnoreCase(ModName))
                        .findFirst()
                        .orElse(null);
                if (webhook == null) {
                    channel.createWebhook(ModName).queue(createWebhook -> {
                        sendToWebhook(createWebhook, username, content);
                    });
                } else {
                    sendToWebhook(webhook, username, content);
                }
            }, error -> {
                System.out.println("Error get webhook. No permission");
            });
        }
    }

    public void sendEmbed(String title, Color color) {
        if (this.jda == null) return;
        var channel = this.jda.getTextChannelById(channelId);

        if (channel != null) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle(title)
                    .setColor(color)
                    .build();

            channel.sendMessageEmbeds(embed).queue();
        }
    }

    private void sendToWebhook(Webhook webhook, String username, String content) {
        webhook.sendMessage(content)
                .setUsername(username)
                .queue();
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
