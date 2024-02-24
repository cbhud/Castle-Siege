package me.cbhud.Commands;

import me.cbhud.Main;
import me.cbhud.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamSpawnCommand implements CommandExecutor {
    private final Main plugin;

    public TeamSpawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("viking.admin")) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /setspawn <teamName>");
            return true;
        }

        String teamName = args[0].toUpperCase(); // Convert to uppercase to handle case-insensitivity

        try {
            Team team = Team.valueOf(teamName);
            setSpawnLocation(team, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Spawn location for " + team + " set successfully! RESTART THE SERVER BEFORE YOU START");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid team name. Available teams: VIKINGS FRANKS");
        }

        return true;
    }

    private void setSpawnLocation(Team team, org.bukkit.Location location) {
        FileConfiguration config = plugin.getConfig();
        String path = "spawnLocations." + team.toString().toLowerCase();

        // Save spawn location to config
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());

        plugin.saveConfig();
    }
}
