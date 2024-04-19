package me.cbhud.castlesiege;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public boolean isStatsEnabled() {
        return config.getBoolean("stats");
    }

    public String getTitle() {
        return config.getString("scoreboard-title");
    }

    public String getBottomline() {
        return config.getString("scoreboard-bottomline");
    }

    public ChatColor getMainColor() {
        return ChatColor.valueOf(config.getString("main-color").toUpperCase());
    }

    public ChatColor getTitleColor(){
        return ChatColor.valueOf(config.getString("title-color").toUpperCase());
    }

    public ChatColor getBottomColor() {
        return ChatColor.valueOf(config.getString("bottom-color").toUpperCase());
    }

    public ChatColor getSecondaryColor() {
        return ChatColor.valueOf(config.getString("secondary-color").toUpperCase());
    }

    public int getAutoStartPlayers() {
        return config.getInt("auto-start-players", 5);
    }

    public int getAutoStartCountdown() {
        return config.getInt("auto-start-countdown", 60);
    }

    public double getKingHealth() {
        return config.getDouble("king-health", 80.0);
    }

}
