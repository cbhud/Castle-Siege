package me.cbhud.team;

import me.cbhud.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    private final Main plugin;
    private final Map<String, Team> playerTeams; // Map to store player UUIDs and their corresponding teams
    private final int maxPlayersPerTeam;

    public TeamManager(Main plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.playerTeams = new HashMap<>();

        // Read the maxPlayersPerTeam value from the config, default to 5 if not present
        this.maxPlayersPerTeam = config.getInt("maxPlayersPerTeam", 5);
    }

    public boolean joinTeam(Player player, Team team) {
        // Check if the team has reached the maximum number of players
        if (getPlayersInTeam(team) >= maxPlayersPerTeam) {
            player.sendMessage("§cThe " + team + " team is full!");
            return false;
        }

        // Get the current team of the player
        Team currentTeam = getTeam(player);

        // Remove the player from the current team's scoreboard
        if (currentTeam != null) {
            plugin.getScoreboardManager().updateLobbyScoreboardForTeam(currentTeam);
        }

        // Add the player to the new team
        playerTeams.put(player.getUniqueId().toString(), team);

        // Update the lobby scoreboard for the new team
        plugin.getScoreboardManager().updateLobbyScoreboardForTeam(team);

        return true;
    }

    public Team getTeam(Player player) {
        // Get the team for a player
        return playerTeams.get(player.getUniqueId().toString());
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public int getPlayersInTeam(Team team) {
        // Count the number of players in the specified team
        return (int) playerTeams.values().stream().filter(t -> t == team).count();
    }
}
