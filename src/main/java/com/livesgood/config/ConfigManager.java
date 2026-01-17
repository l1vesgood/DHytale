package com.livesgood.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private final Path configPath;
    private final Gson gson;
    private BotConfig config;

    public ConfigManager(Path dataDirectory) {
        this.configPath = dataDirectory.resolve("config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void load() {
        if (!Files.exists(configPath)) {
            createDefaultConfig();
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            this.config = gson.fromJson(reader, BotConfig.class);
            if (this.config == null) {
                createDefaultConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() {
        java.util.Map<String, MessagesConfig> messages = new java.util.HashMap<>();
        
        messages.put("en", new MessagesConfig(
            "{player} has joined the server",
            "{player} has left the server"
        ));

        messages.put("ru", new MessagesConfig(
            "{player} присоединился к игре",
            "{player} покинул игру"
        ));

        this.config = new BotConfig("YOUR_BOT_TOKEN", "YOUR_CHANNEL_ID", "en", messages);
        save();
    }

    public void save() {
        try {
            if (Files.notExists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                gson.toJson(this.config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BotConfig getConfig() {
        return config;
    }
}
