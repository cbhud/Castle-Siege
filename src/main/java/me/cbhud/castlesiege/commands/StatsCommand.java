package me.cbhud.castlesiege.commands;

import me.cbhud.castlesiege.CastleSiege;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsCommand implements CommandExecutor {

    private final CastleSiege plugin;
    ChatColor main;
    ChatColor second;

    public StatsCommand(CastleSiege plugin) {

        this.plugin = plugin;
        this.main = plugin.getConfigManager().getMainColor();
        this.second = plugin.getConfigManager().getSecondaryColor();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }
            Player player = (Player) sender;
            String username = player.getName();
            sendStats(sender, username);
            return true;
        } else if (args.length == 1) {
            String username = args[0];
            sendStats(sender, username);
            return true;
        } else {
            sender.sendMessage("Usage: /stats [player]");
            return true;
        }
    }

    private void sendStats(CommandSender sender, String username) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT wins, kills, deaths, king_kills, coins FROM player_stats WHERE username=? LIMIT 1"; // Add LIMIT 1 to ensure only one result is returned
            synchronized (plugin.getDbConnection().getConnection()) {
                statement = plugin.getDbConnection().getConnection().prepareStatement(sql);
            }
            statement.setString(1, username);

            synchronized (plugin.getDbConnection().getConnection()) {
                resultSet = statement.executeQuery();
            }

            if (resultSet.next()) {
                int wins = resultSet.getInt("wins");
                int kills = resultSet.getInt("kills");
                int deaths = resultSet.getInt("deaths");
                double kdr = (deaths == 0) ? kills : ((double) kills / deaths); // Calculate KDR (handle division by zero)
                String formattedKdr = String.format("%.2f", kdr);  // Round to 2 decimal places);
                int kingKills = resultSet.getInt("king_kills");
                int coins = resultSet.getInt("coins");

                sender.sendMessage(main + "Stats for: " + second + username);
                sender.sendMessage(main + "Wins: " + second + wins);
                sender.sendMessage(main + "Kills: " + second + kills);
                sender.sendMessage(main + "Deaths: " + second + deaths);
                sender.sendMessage(main + "KDR: " + second + formattedKdr);
                sender.sendMessage(main + "King Kills: " + second + kingKills);
                sender.sendMessage(main + "Coins " + second + coins);
            } else {
                sender.sendMessage(ChatColor.RED + "Player " + username + " has no stats recorded.");
            }
        } catch (SQLException e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while fetching player stats.");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}