package me.cbhud.kits;

import me.cbhud.Main;
import me.cbhud.kits.KitType;
import me.cbhud.scoreboard.ScoreboardManager;
import me.cbhud.spectator.PlayerStates;
import me.cbhud.state.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    private final Main plugin;

    public KitCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("kit").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the game is in progress
        if (plugin.getGame().getState() == GameState.LOBBY) {
            // Check if the player is not a spectator
            if (plugin.getPlayerStateManager().getPlayerState(player) != PlayerStates.SPECTATOR) {
                // Check if the player has already selected a kit
                    // Check if the correct number of arguments is provided
                    if (args.length == 1) {
                        // Attempt to parse the kit type from the argument
                        KitType kitType = KitType.getByName(args[0]);

                        if (kitType != null) {
                            if (plugin.getPlayerKitManager().getSelectedKit(player) == kitType){
                                return true;
                            }
                            if (kitType.getTeam() == plugin.getTeamManager().getTeam(player)) {
                                // Give the player the selected kit
                                plugin.getPlayerKitManager().selectKit(player, kitType);
                                player.sendMessage("§aYou have selected " + kitType + " kit.");
                                plugin.getScoreboardManager().updateScoreboard(player);
                            } else {
                                player.sendMessage(ChatColor.RED + "You cannot select a kit from the opposing team.");
                            }
                        }
                        else {
                            player.sendMessage("§cInvalid kit!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /kit <kitName>");
                    }

            } else {
                player.sendMessage("You cannot select kit as spectator");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You can only select kits in Lobby.");
        }

        return true;
    }
}
