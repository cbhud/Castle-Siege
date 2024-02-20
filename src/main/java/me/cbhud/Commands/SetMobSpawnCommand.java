package me.cbhud.Commands;

import me.cbhud.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMobSpawnCommand implements CommandExecutor {

    private final Main plugin;

    public SetMobSpawnCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Save individual components of the spawn location to the configuration file
            plugin.getConfig().set("mobSpawnLocation.x", player.getLocation().getX());
            plugin.getConfig().set("mobSpawnLocation.y", player.getLocation().getY());
            plugin.getConfig().set("mobSpawnLocation.z", player.getLocation().getZ());
            plugin.getConfig().set("mobSpawnLocation.yaw", player.getLocation().getYaw());
            plugin.getConfig().set("mobSpawnLocation.pitch", player.getLocation().getPitch());
            plugin.getConfig().set("mobSpawnLocation.world", player.getLocation().getWorld().getName());
            plugin.saveConfig();

            player.sendMessage("Mob spawn location set RESTART THE SERVER BEFORE YOU START!");
        } else {
            sender.sendMessage("Only players can use this command.");
        }

        return true;
    }

}


