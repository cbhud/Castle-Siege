package me.cbhud.event;

import me.cbhud.Autostart;
import me.cbhud.ConfigManager;
import me.cbhud.Main;
import me.cbhud.state.Game;
import me.cbhud.state.GameState;
import me.cbhud.team.Team;
import me.cbhud.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerJoin implements Listener {
    private final Game game;
    private final Main plugin;
    private final TeamManager teamManager;
    private final Autostart autostartInstance; // Add this line
    private final ConfigManager configManager;

    private Location lobbyLocation; // Add this line

    public PlayerJoin(Main plugin, Game game, TeamManager teamManager, Autostart autostartInstance, ConfigManager configManager) {
        this.plugin = plugin;
        this.game = game;
        this.teamManager = teamManager;
        this.autostartInstance = autostartInstance;
        this.configManager = configManager;

        // Load lobby location during initialization
        loadLobbyLocation();
    }

    private void loadLobbyLocation() {
        FileConfiguration config = plugin.getConfig();

        if (config.contains("lobby.world")) {
            String worldName = config.getString("lobby.world");
            double x = config.getDouble("lobby.x");
            double y = config.getDouble("lobby.y");
            double z = config.getDouble("lobby.z");
            float yaw = (float) config.getDouble("lobby.yaw");
            float pitch = (float) config.getDouble("lobby.pitch");

            lobbyLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            // Handle the case when the lobby location is not set in the config
            plugin.getLogger().warning("Lobby location is not set in the config.");
            lobbyLocation = null; // Set to null or another default value
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGame().getState() == GameState.LOBBY) {
            teleportToLobby(player);
            plugin.getPlayerManager().setPlayerAsLobby(player);
            plugin.getScoreboardManager().setupScoreboard(player);
            if (Bukkit.getOnlinePlayers().size() >= configManager.getAutoStartPlayers()){
                autostartInstance.checkAutoStart(Bukkit.getOnlinePlayers().size());
            }
            else if(configManager.getAutoStartPlayers() - Bukkit.getOnlinePlayers().size() == 1){
                Bukkit.broadcastMessage(configManager.getMainColor() + "The game requires " + ChatColor.WHITE + (configManager.getAutoStartPlayers() - Bukkit.getOnlinePlayers().size()) + configManager.getMainColor() + " more player to start.");
            }
            else {
                Bukkit.broadcastMessage(configManager.getMainColor() + "The game requires " + ChatColor.WHITE + (configManager.getAutoStartPlayers() - Bukkit.getOnlinePlayers().size()) + configManager.getMainColor() + " more players to start.");
            }

            if (!tryRandomTeamJoin(player)) {
                player.sendTitle(ChatColor.GRAY + "Both teams are full, ", ChatColor.GRAY + "wait until the game finishes in spectator.", 10, 70, 20);
                plugin.getScoreboardManager().setupScoreboard(player);
                plugin.getPlayerManager().setPlayerAsSpectator(player);
                teleportToLobby(player);
            }
        } else {
            plugin.getScoreboardManager().setupScoreboard(player);
            player.sendTitle(ChatColor.GRAY + "You are spectating now!", ChatColor.GRAY + "Wait until the game finishes.", 10, 70, 20);
            plugin.getPlayerManager().setPlayerAsSpectator(player);
            teleportToLobby(player);
        }
    }

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

    private void teleportToLobby(Player player) {
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
        } else {
            // Handle the case when the lobby location is not set
            player.sendMessage(ChatColor.RED + "Lobby location is not set. /setlobby");
            player.sendMessage(ChatColor.RED + "Also don't forget to set spawns for teams and king");
            player.sendMessage(ChatColor.RED + "/setmobspawn");
            player.sendMessage(ChatColor.RED + "/setspawn VIKINGS or FRANKS");
        }
    }
}
