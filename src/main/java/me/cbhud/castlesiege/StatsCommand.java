package me.cbhud.castlesiege;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.sql.*;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final DbConnection dbConnection;

    public StatsCommand(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // If no argument is provided, show stats of the player who issued the command
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }
            Player player = (Player) sender;
            String username = player.getName();
            sendStats(sender, username);
            return true;
        } else if (args.length == 1) {
            // If a player's username is provided as an argument, show their stats
            String username = args[0];
            sendStats(sender, username);
            return true;
        } else {
            // Incorrect command usage
            sender.sendMessage("Usage: /stats [player]");
            return true;
        }
    }

    private void sendStats(CommandSender sender, String username) {
        try {
            // Prepare the SQL query
            String sql = "SELECT * FROM player_stats WHERE username=?";
            PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if the player exists in the database
            if (resultSet.next()) {
                int wins = resultSet.getInt("wins");
                int kills = resultSet.getInt("kills");
                int deaths = resultSet.getInt("deaths");
                double kdr = (double) resultSet.getInt("kills") / resultSet.getInt("deaths");
                int kingKills = resultSet.getInt("king_kills");

                // Send player stats to the sender
                sender.sendMessage(ChatColor.YELLOW + "Stats for: " + ChatColor.AQUA + username);
                sender.sendMessage(ChatColor.AQUA + "Wins: " + ChatColor.WHITE + wins);
                sender.sendMessage(ChatColor.AQUA + "Kills: " +ChatColor.WHITE + kills);
                sender.sendMessage(ChatColor.AQUA + "Deaths: " +ChatColor.WHITE + deaths);
                sender.sendMessage(ChatColor.AQUA + "KDR: " +ChatColor.WHITE + kdr);
                sender.sendMessage(ChatColor.AQUA + "King Kills: " +ChatColor.WHITE + kingKills);
            } else {
                sender.sendMessage(ChatColor.RED + "Player " + username + " has no stats recorded.");
            }

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            sender.sendMessage("An error occurred while fetching player stats.");
            e.printStackTrace();
        }
    }
}
