package me.cbhud;

import me.cbhud.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import java.util.ArrayList;
import java.util.List;

public class Autostart {
    private final Main plugin;
    private final ConfigManager configManager;
    private int playersToStart;
    private int initialCountdownSeconds;
    private int countdownSeconds;
    private int taskId;
    private List<Integer> taskIds = new ArrayList<>();
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public Autostart(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playersToStart = configManager.getConfig().getInt("auto-start-players");
        this.initialCountdownSeconds = configManager.getConfig().getInt("auto-start-countdown", 60);
        this.countdownSeconds = initialCountdownSeconds;
    }

    public void checkAutoStart(int currentPlayers) {
        if (currentPlayers >= playersToStart) {
            startCountdown();
        } else {
            cancelCountdown();
        }
    }

    private void startCountdown() {
        resetCountdown(); // Reset countdown to initial value
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (countdownSeconds > 0) {
                if (countdownSeconds == initialCountdownSeconds || countdownSeconds == 45 || countdownSeconds == 30 || countdownSeconds == 15 || countdownSeconds == 10 || countdownSeconds <= 5) {
                    Bukkit.broadcastMessage(configManager.getMainColor() + "Game is starting in " + ChatColor.WHITE + countdownSeconds + configManager.getMainColor() + " seconds!");
                }
                countdownSeconds--;
            } else {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting the game!");
                String command = "start";
                Bukkit.dispatchCommand(console, command);
                cancelCountdown(); // Cancel the repeating task after starting the game
            }
        }, 0L, 20L); // 20 ticks per second

        taskIds.add(taskId); // Store the task ID
    }

    private void cancelCountdown() {
        for (int taskId : taskIds) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        taskIds.clear(); // Clear the list
        if(plugin.getGame().getState() == GameState.IN_GAME){
        }
        else {
        Bukkit.broadcastMessage(ChatColor.RED + "There are not enough players to start the game!");
    }}
    private void resetCountdown() {
        countdownSeconds = initialCountdownSeconds;
    }
}