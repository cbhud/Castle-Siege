package me.cbhud;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;

public class CountdownTimer {
    private final Main plugin;
    private int secondsLeft;
    private BukkitRunnable timerTask;
    private Team winner;

    public CountdownTimer(Main plugin) {
        this.plugin = plugin;
    }

    public void startTimer(int minutes) {
        secondsLeft = minutes * 60;

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (secondsLeft > 0) {
                    secondsLeft--;
                    if (plugin.getGame().getState() == GameState.IN_GAME) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            FastBoard board = plugin.getScoreboardManager().scoreboards.get(player);
                            if (board != null) {
                                plugin.getScoreboardManager().updateInGameScoreboard(board);
                            }
                        });
                    }
                } else {
                    endTimer();
                }
            }
        };
        timerTask.runTaskTimer(plugin, 0, 20); // Run every second (20 ticks)

        //Bukkit.broadcastMessage("The game has started! Timer set to " + minutes + " minutes.");
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void endTimer() {
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            plugin.getWinner().setWinner(Team.Franks);
            plugin.getGameEndHandler().handleGameEnd();
            cancelTimer();
        }
    }

    public void cancelTimer() {
        if (timerTask != null) {
            timerTask.cancel(); // Stop the timer
            timerTask = null; // Set timerTask to null after canceling
        }
    }

    public int getSecondsLeft() {
        return secondsLeft;
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
