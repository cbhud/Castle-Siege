package me.cbhud.state;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.cbhud.Main;
import org.bukkit.scoreboard.ScoreboardManager;

public class Game {
    private final Main plugin; // Add this field
    private GameState currentState;

    public Game(Main plugin) {
        this.plugin = plugin; // Initialize the field

        // Set the initial state to LOBBY
        currentState = GameState.LOBBY;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        plugin.getScoreboardManager().updateScoreboardForAll();
    }

    public GameState getState() {
        return currentState;
    }



    }


