package me.cbhud.castlesiege.game;
import me.cbhud.castlesiege.CastleSiege;

public class Game {
    private final CastleSiege plugin;
    private GameState currentState;
    private Type typeState;

    public Game(CastleSiege plugin) {
        this.plugin = plugin;
        typeState = Type.Normal;
        currentState = GameState.LOBBY;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        plugin.getScoreboardManager().updateScoreboardForAll();
    }

    public GameState getState() {
        return currentState;
    }


    ///


    public void setType(Type newState) {
        this.typeState = newState;
        plugin.getScoreboardManager().updateScoreboardForAll();
    }

    public Type getType() {
        return typeState;
    }


    @Override
    public String toString(){
        return currentState.toString().substring(0, 1).toUpperCase() + typeState.toString().substring(1).toLowerCase();
    }



}


