package me.cbhud.castlesiege.commands;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnlockKitCommand implements CommandExecutor {

    private final CastleSiege plugin;

    public UnlockKitCommand(CastleSiege plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp() && !player.hasPermission("cs.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /kit <unlock | lock> <kit> <player>");
            return true;
        }

        String action = args[0];
        KitManager.KitData kitName = plugin.getKitManager().getKitByName(args[1]);
        String targetPlayerName = args[2];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        UUID targetUUID = targetPlayer.getUniqueId();

        switch (action.toLowerCase()) {
            case "unlock":
                plugin.getDbConnection().unlockPlayerKit(targetUUID, kitName);
                player.sendMessage(ChatColor.GREEN + "You have unlocked kit " + kitName.getName() + " for player" + player.getName() + ".");
                targetPlayer.sendMessage(ChatColor.GREEN + "You unlocked kit " + kitName.getName() + ".");
                break;

            case "lock":
                plugin.getDbConnection().lockPlayerKit(targetUUID, kitName);
                player.sendMessage(ChatColor.GREEN + "You have unlocked kit " + kitName.getName() + " for player" + player.getName() + ".");
                targetPlayer.sendMessage(ChatColor.GREEN + "You unlocked kit " + kitName.getName() + ".");
                break;

            default:
                player.sendMessage(ChatColor.RED + "Invalid action. Use: set, add, or remove.");
                break;
        }

        return true;
    }
}
