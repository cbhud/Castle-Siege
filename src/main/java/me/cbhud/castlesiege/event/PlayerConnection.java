package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.CastleSiege;

import me.cbhud.castlesiege.state.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerConnection implements Listener {
    private final CastleSiege plugin;


    public PlayerConnection(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getScoreboardManager().setupScoreboard(player);
        if(!plugin.getDbConnection().hasData(player.getUniqueId())){
            plugin.getDbConnection().createProfileIfNotExists(player.getUniqueId(), player.getName());
        }
        if (plugin.getGame().getState() == GameState.LOBBY) {
            tryRandomTeamJoin(player);
            teleport(player, plugin.getLocationManager().getLobbyLocation(), "Lobby");
            plugin.getPlayerManager().setPlayerAsLobby(player);
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            if (onlinePlayers >= plugin.getConfigManager().getAutoStartPlayers()) {
                plugin.getTimer().checkAutoStart(onlinePlayers);
            } else if (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers == 1) {
                Bukkit.broadcastMessage(plugin.getConfigManager().getMainColor() + "The game requires " + ChatColor.WHITE + (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers) + plugin.getConfigManager().getMainColor() + " more player to start.");
            } else {
                Bukkit.broadcastMessage(plugin.getConfigManager().getMainColor() + "The game requires " + ChatColor.WHITE + (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers) + plugin.getConfigManager().getMainColor() + " more players to start.");
            }
            if (!tryRandomTeamJoin(player)) {
                player.sendTitle(ChatColor.GRAY + "Both teams are full, ", ChatColor.GRAY + "wait until the game finishes in spectator.", 10, 70, 20);
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                teleport(player, plugin.getLocationManager().getLobbyLocation(), "Lobby");
            }
        } else {
            player.sendTitle(ChatColor.GRAY + "You are spectating now!", ChatColor.GRAY + "Wait until the game finishes.", 10, 70, 20);
            plugin.getPlayerManager().setPlayerAsSpectator(player);
            teleport(player, plugin.getLocationManager().getMobLocation(), "Mob spawn location");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGame().getState() == GameState.IN_GAME) {
            plugin.getScoreboardManager().decrementTeamPlayersCount(player);
            if (plugin.getScoreboardManager().getVikings() < 1 && plugin.getScoreboardManager().getFranks() < 1) {
                plugin.getGameEndHandler().setWinner(null);
                plugin.getGameEndHandler().handleGameEnd();
            } else if (plugin.getScoreboardManager().getVikings() < 1) {
                plugin.getGameEndHandler().setWinner(Team.Defenders);
                plugin.getGameEndHandler().handleGameEnd();
            }
        }
        plugin.getScoreboardManager().removeScoreboard(player);
        plugin.getTeamManager().removeTeam(player);
        if (plugin.getGame().getState() == GameState.LOBBY) {
            Bukkit.getScheduler().runTask(plugin, () -> plugin.getTimer().checkAutoStart(Bukkit.getOnlinePlayers().size()));
        }
    }

    public boolean tryRandomTeamJoin(Player player) {
        List<Team> teams = Arrays.asList(Team.values());
        Collections.shuffle(teams);
        for (Team team : teams) {
            if (plugin.getTeamManager().getPlayersInTeam(team) < plugin.getTeamManager().getMaxPlayersPerTeam()) {
                plugin.getTeamManager().joinTeam(player, team);
                return true;
            }
        }
        return false;
    }

    private void teleport(Player player, Location location, String locationName) {
        if (location != null) {
            player.teleport(location);
        } else {
            player.sendMessage(ChatColor.RED + locationName + " location is not set.");
            player.sendMessage(ChatColor.RED + "Please configure it properly.");
        }
    }
}
