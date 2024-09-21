package me.cbhud.castlesiege.util;
import me.cbhud.castlesiege.CastleSiege;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MessagesConfiguration {
    private final CastleSiege plugin;
    private FileConfiguration config;
    private File configFile;

    public MessagesConfiguration(CastleSiege plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<String> getStartMsg() {
        return getMessages("startmsg");
    }

    public List<String> getNotStartMsg() {
        return getMessages("notenoughtostart");
    }
    public List<String> getFranksWinMsg() {
        return getMessages("defenderswinmsg");
    }

    public List<String> getVikingsWinMsg() {
        return getMessages("attackerswinmsg");
    }

    public List<String> getHardcoreMsg() {
        return getMessages("hardcoremsg");
    }

    public List<String> getHardCoreEnabledMsg() {
        return getMessages("hardcoreEnabled");
    }

    public List<String> getForceStopMsg() {
        return getMessages("forcestopmsg");
    }

    public List<String> getCooldown() {
        return getMessages("tntcooldown");
    }


    private List<String> getMessages(String key) {
        List<String> messages = config.getStringList(key);
        return messages.stream()
                .map(message -> ChatColor.translateAlternateColorCodes('&', message))
                .collect(Collectors.toList());
    }
}
