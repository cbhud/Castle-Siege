package me.cbhud.castlesiege;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import java.util.ArrayList;
import java.util.List;

public class Timers {
    private final Main plugin;
    private final ConfigManager configManager;
    private final int playersToStart;
    private final int initialCountdownSeconds;
    private int countdownSeconds;
    private int taskId;
    private final List<Integer> taskIds = new ArrayList<>();
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private BukkitRunnable timerTask;
    private Team winner;

    public Timers(Main plugin, ConfigManager configManager) {
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
        resetCountdown();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (countdownSeconds > 0) {
                if (countdownSeconds == initialCountdownSeconds || countdownSeconds == 45 || countdownSeconds == 30 || countdownSeconds == 15 || countdownSeconds == 10 || countdownSeconds <= 5) {
                    Bukkit.broadcastMessage(configManager.getMainColor() + "Game is starting in " + ChatColor.WHITE + countdownSeconds + configManager.getMainColor() + " seconds!");
                }
                countdownSeconds--;
            } else {
                cancelCountdown(); // Cancel the repeating task after starting the game
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting the game!");
                String command = "cs start";
                Bukkit.dispatchCommand(console, command);
            }
        }, 0L, 20L); // 20 ticks per second

        taskIds.add(taskId); // Store the task ID
    }

    private void cancelCountdown() {
        for (int taskId : taskIds) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        taskIds.clear(); // Clear the list
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            plugin.getGameEndHandler().setWinner(Team.Franks);
            plugin.getGameEndHandler().handleGameEnd();
            cancelTimer();
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "There are not enough players to start the game!");
        }
    }

    private void resetCountdown() {
        countdownSeconds = initialCountdownSeconds;
    }

    public void startTimer(int minutes) {
        countdownSeconds = minutes * 60;

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (countdownSeconds > 0) {
                    countdownSeconds--;
                    if (plugin.getGame().getState() == GameState.IN_GAME) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            FastBoard board = plugin.getScoreboardManager().scoreboards.get(player);
                            if (board != null) {
                                plugin.getScoreboardManager().updateInGameScoreboard(board);
                            }
                        });
                    }
                } else {
                    if (plugin.getGame().getState() == GameState.IN_GAME) {
                        plugin.getGameEndHandler().setWinner(Team.Franks);
                        plugin.getGameEndHandler().handleGameEnd();
                        cancelTimer();
                    }
                }
            }
        };
        timerTask.runTaskTimer(plugin, 0, 20); // Run every second (20 ticks)

        //Bukkit.broadcastMessage("The game has started! Timer set to " + minutes + " minutes.");
    }

    public void cancelTimer() {
        if (timerTask != null) {
            timerTask.cancel(); // Stop the timer
            timerTask = null; // Set timerTask to null after canceling
        }
    }

    public int getSecondsLeft() {
        return countdownSeconds;
    }

    public GameState getGameState() {
        return plugin.getGame().getState();
    }

    public Team getWinner() {
        return winner;
    }

    public BukkitRunnable getTimerTask() {
        return timerTask;
    }
}
