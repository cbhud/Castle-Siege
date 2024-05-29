package me.cbhud.castlesiege.playerstate;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.gui.Manager;
import me.cbhud.castlesiege.team.Team;
import me.cbhud.castlesiege.team.TeamManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.cbhud.castlesiege.playerstate.PlayerStates.*;

public class PlayerManager {

    private final Main plugin;
    private final TeamManager teamManager;
    private final Map<Player, PlayerStates> playerStates;

    public PlayerManager(Main plugin, TeamManager teamManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
        this.playerStates = new HashMap<>();
    }

    public PlayerStates getPlayerState(Player player) {
        if (player != null) {
            return playerStates.getOrDefault(player, PLAYERLOBBY);
        }
        return PLAYERLOBBY;
    }

    public void setPlayerAsPlaying(Player player) {
        playerStates.put(player, PLAYING);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTask(plugin);
    }

    public void setPlayerAsLobby(Player player) {
        playerStates.put(player, PLAYERLOBBY);
        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (teamManager.getTeam(player) == null) {
            tryRandomTeamJoin(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTask(plugin);

        player.getInventory().clear();
        player.setHealth(20);
        player.setLevel(0);
        player.getActivePotionEffects().clear();

        player.getInventory().setItem(3, Manager.clock);
        player.getInventory().setItem(5, Manager.star);
    }

    public void setPlayerAsSpectator(Player player) {
        playerStates.put(player, SPECTATOR);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }.runTask(plugin);
    }

    private boolean tryRandomTeamJoin(Player player) {
        Team[] teams = Team.values();
        List<Team> teamList = Arrays.asList(teams);
        Collections.shuffle(teamList);

        for (Team team : teamList) {
            if (teamManager.getPlayersInTeam(team) < teamManager.getMaxPlayersPerTeam()) {
                teamManager.joinTeam(player, team);
                return true;
            }
        }
        return false;
    }
}
