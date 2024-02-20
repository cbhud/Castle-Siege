package me.cbhud.team;

import me.cbhud.Main;
import me.cbhud.state.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamJoinCommand implements CommandExecutor {
    private final Main plugin;
    private final TeamManager teamManager;

    public TeamJoinCommand(Main plugin, TeamManager teamManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the game state is LOBBY
        if (plugin.getGame().getState() != GameState.LOBBY) {
            player.sendMessage("You cannot change teams while in-game.");
            return true;
        }

        if (args.length != 1 || args[0] == null) {
            player.sendMessage("Usage: /teamjoin <teamName>");
            return true;
        }

        String teamName = args[0].toUpperCase(); // Convert to uppercase to handle case-insensitivity

        try {
            Team requestedTeam = Team.valueOf(teamName);

            // Check if the player is already in the requested team
            Team currentTeam = teamManager.getTeam(player);
            if (currentTeam == requestedTeam) {
                return true;
            }


            if (teamManager.joinTeam(player, requestedTeam)) {
                player.sendMessage("§aYou joined " + teamName + " team!");
                plugin.getPlayerKitManager().setDefaultKit(player, requestedTeam);
                plugin.getScoreboardManager().updateScoreboard(player);
            } else {
                player.sendMessage("Failed to join the team. Make sure the team name is correct.");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("Available teams: VIKINGS, FRANKS");
        }

        return true;
    }
}
