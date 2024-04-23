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
import java.util.List;

import static me.cbhud.castlesiege.playerstate.PlayerStates.*;

public class PlayerManager {

    private final Main plugin;
    private final TeamManager teamManager;
    private final PlayerStateManager playerStateManager;

    public PlayerManager(Main plugin, PlayerStateManager playerStateManager, TeamManager teamManager) {
        this.plugin = plugin;
        this.playerStateManager = playerStateManager;
        this.teamManager = teamManager;
    }

    public void setPlayerState(Player player, PlayerStates state) {
        switch (state) {
            case PLAYING:
                setPlayerAsPlaying(player);
                break;
            case SPECTATOR:
                setPlayerAsSpectator(player);
                break;
            case PLAYERLOBBY:
                setPlayerAsLobby(player);
                break;
            default:
                break;
        }
    }

    public void setPlayerAsPlaying(Player player) {
        plugin.getPlayerStateManager().setPlayerState(player, PLAYING);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL); // Example: Setting player's game mode to survival
            }
        }.runTask(plugin);
    }

    public void setPlayerAsLobby(Player player) {
        plugin.getPlayerStateManager().setPlayerState(player, PLAYERLOBBY);
        if(player.getGameMode() != GameMode.SURVIVAL){
            player.setGameMode(GameMode.SURVIVAL);
        }
        if(plugin.getTeamManager().getTeam(player) == null){
            tryRandomTeamJoin(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL); // Example: Setting player's game mode to survival
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
        plugin.getPlayerStateManager().setPlayerState(player, SPECTATOR);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SPECTATOR); // Example: Setting player's game mode to survival
            }
        }.runTask(plugin);    }

    private boolean tryRandomTeamJoin(Player player) {
        // Get all available teams
        Team[] teams = Team.values();

        // Shuffle the array to randomize team selection
        List<Team> teamList = Arrays.asList(teams);
        Collections.shuffle(teamList);

        // Try to join each team until successful or all teams are full
        for (Team team : teamList) {
            if (teamManager.getPlayersInTeam(team) < teamManager.getMaxPlayersPerTeam()) {
                // Join the team and return true
                teamManager.joinTeam(player, team);
                return true;
            }
        }

        // Failed to join any team
        return false;
    }

    }


