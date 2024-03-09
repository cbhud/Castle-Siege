package me.cbhud.Events;

import me.cbhud.Autostart;
import me.cbhud.ConfigManager;
import me.cbhud.Main;
import me.cbhud.scoreboard.ScoreboardManager;
import me.cbhud.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeft implements Listener {

    private final Main plugin;
    private final ConfigManager configManager;
    private final Autostart autostart;

    public PlayerLeft(Main plugin, ConfigManager configManager, Autostart autostart) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.autostart = autostart;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getGame().getState() == GameState.LOBBY) {
            Bukkit.getScheduler().runTask(plugin, () -> autostart.checkAutoStart(Bukkit.getOnlinePlayers().size()));
        }
    }
}
