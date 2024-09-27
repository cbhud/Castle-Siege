package me.cbhud.castlesiege.state;
import me.cbhud.castlesiege.CastleSiege;


public class Game {
    private final CastleSiege plugin;
    private GameState currentState;

    public Game(CastleSiege plugin) {
        this.plugin = plugin;

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


