package me.cbhud.castlesiege;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.util.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CoinsCommand implements CommandExecutor {

    private final Main plugin;
    private final DataManager dataManager;

    public CoinsCommand(Main plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getDbConnection();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the required permissions
        if (!player.isOp() && !player.hasPermission("cs.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Ensure correct usage
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /coins <set|add|remove> <player> <amount>");
            return true;
        }

        String action = args[0];
        String targetPlayerName = args[1];
        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "The amount must be a number.");
            return true;
        }

        // Find the target player
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        UUID targetUUID = targetPlayer.getUniqueId();

        switch (action.toLowerCase()) {
            case "set":
                dataManager.setPlayerCoins(targetUUID, amount);
                player.sendMessage(ChatColor.GREEN + "You have set " + targetPlayerName + "'s coins to " + amount + ".");
                targetPlayer.sendMessage(ChatColor.GREEN + "Your coins have been set to " + amount + " by " + player.getName() + ".");
                break;

            case "add":
                dataManager.addPlayerCoins(targetUUID, amount);
                player.sendMessage(ChatColor.GREEN + "You have added " + amount + " coins to " + targetPlayerName + ".");
                targetPlayer.sendMessage(ChatColor.GREEN + "You have received " + amount + " coins from " + player.getName() + ".");
                break;

            case "remove":
                if (dataManager.removePlayerCoins(targetUUID, amount)) {
                    player.sendMessage(ChatColor.GREEN + "You have removed " + amount + " coins from " + targetPlayerName + ".");
                    targetPlayer.sendMessage(ChatColor.RED + "You have lost " + amount + " coins, removed by " + player.getName() + ".");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to remove coins. " + targetPlayerName + " does not have enough coins.");
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Invalid action. Use: set, add, or remove.");
                break;
        }

        return true;
    }
}
