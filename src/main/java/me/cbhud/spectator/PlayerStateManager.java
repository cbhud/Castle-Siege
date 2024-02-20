package me.cbhud.spectator;

import org.bukkit.entity.Player;
import org.bukkit.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerStateManager {

    private final Map<Player, PlayerStates> playerStates;

    public PlayerStateManager() {
        this.playerStates = new HashMap<>();
    }

    public PlayerStates getPlayerState(Player player) {
        if (player != null) {
            return playerStates.getOrDefault(player, PlayerStates.PLAYERLOBBY);
        }
        return playerStates.getOrDefault(player, PlayerStates.PLAYERLOBBY);
    }

    public void setPlayerState(Player player, PlayerStates state) {
        playerStates.put(player, state);
    }

    public void removePlayerState(Player player) {
        playerStates.remove(player);
    }
}
