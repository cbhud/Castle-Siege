package me.cbhud.castlesiege.event;

import me.cbhud.castlesiege.CastleSiege;

import me.cbhud.castlesiege.game.GameState;
import me.cbhud.castlesiege.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener {
    private final CastleSiege plugin;

    public PlayerConnection(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!plugin.getDbConnection().hasData(player.getUniqueId())){
            plugin.getDbConnection().createProfile(player.getUniqueId(), player.getName());
        }
        plugin.getScoreboardManager().setupScoreboard(player);
        if (plugin.getGame().getState() == GameState.LOBBY) {
            plugin.getPlayerManager().setPlayerAsLobby(player);
            plugin.getLocationManager().teleport(player, plugin.getLocationManager().getLobbyLocation(), "Lobby");
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            if (onlinePlayers >= plugin.getConfigManager().getAutoStartPlayers()) {
                plugin.getTimer().checkAutoStart(onlinePlayers);
            } else if (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers == 1) {
                Bukkit.broadcastMessage(plugin.getConfigManager().getMainColor() + "The game requires " + ChatColor.WHITE + (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers) + plugin.getConfigManager().getMainColor() + " more player to start.");
            } else {
                Bukkit.broadcastMessage(plugin.getConfigManager().getMainColor() + "The game requires " + ChatColor.WHITE + (plugin.getConfigManager().getAutoStartPlayers() - onlinePlayers) + plugin.getConfigManager().getMainColor() + " more players to start.");
            }
        } else {
            player.sendTitle(ChatColor.GRAY + "You are now a spectator.", ChatColor.GRAY + "Please wait until the game concludes.", 10, 70, 20);
            plugin.getPlayerManager().setPlayerAsSpectator(player);
            plugin.getLocationManager().teleport(player, plugin.getLocationManager().getMobLocation(), "Mob spawn location");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getScoreboardManager().removeScoreboard(player);
        if (plugin.getGame().getState().equals(GameState.IN_GAME)) {
            plugin.getScoreboardManager().decrementTeamPlayersCount(player);
            plugin.getTeamManager().removeTeam(player);
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                plugin.getGameEndHandler().setWinner(null);
                plugin.getGameEndHandler().handleGameEnd();
            } else if (plugin.getScoreboardManager().getVikings() < 1) {
                plugin.getGameEndHandler().setWinner(Team.Defenders);
                plugin.getGameEndHandler().handleGameEnd();
            }
        }

        if (plugin.getGame().getState() == GameState.LOBBY) {
            Bukkit.getScheduler().runTask(plugin, () -> plugin.getTimer().checkAutoStart(Bukkit.getOnlinePlayers().size()));
        }
    }





}
