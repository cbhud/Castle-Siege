package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.Main;
import me.cbhud.castlesiege.kits.KitType;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.UUID;

public class DataManager {
    private final Main plugin;
    private Connection connection;

    public DataManager(Main plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            // Create an H2 data source
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/data");

            // Connect to H2 database
            connection = dataSource.getConnection();

            // Create the tables if they don't exist
            createTables();

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() {
        // Create the tables based on the new structure
        try (Statement statement = connection.createStatement()) {
            // Create player_stats table
            String createPlayerStatsTableSQL = "CREATE TABLE IF NOT EXISTS player_stats (" +
                    "uuid VARCHAR(36) PRIMARY KEY," +
                    "username VARCHAR(16) NOT NULL," +
                    "wins INT NOT NULL," +
                    "kills INT NOT NULL," +
                    "deaths INT NOT NULL," +
                    "king_kills INT NOT NULL," +
                    "coins INT NOT NULL" +
                    ")";
            statement.executeUpdate(createPlayerStatsTableSQL);

            // Create player_kits table
            String createPlayerKitsTableSQL = "CREATE TABLE IF NOT EXISTS player_kits (" +
                    "uuid VARCHAR(36) PRIMARY KEY," +
                    "MARKSMAN BOOLEAN NOT NULL," +
                    "SKALD BOOLEAN NOT NULL," +
                    "BOMBARDIER BOOLEAN NOT NULL," +
                    "BERSERKER BOOLEAN NOT NULL," +
                    "WARRIOR BOOLEAN NOT NULL," +
                    "WIZARD BOOLEAN NOT NULL," +
                    "SPEARMAN BOOLEAN NOT NULL," +
                    "KNIGHT BOOLEAN NOT NULL," +
                    "FOREIGN KEY (uuid) REFERENCES player_stats(uuid)" +
                    ")";
            statement.executeUpdate(createPlayerKitsTableSQL);

            plugin.getLogger().info("Tables created successfully!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean hasData(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM player_stats WHERE uuid = ? LIMIT 1")) {
            statement.setString(1, uuid.toString());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createProfileIfNotExists(UUID playerUUID, String username) {
        if (!hasData(playerUUID)) {
            // Insert the player profile into the player_stats table
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO player_stats (uuid, username, wins, kills, deaths, king_kills, coins) " +
                            "VALUES (?, ?, 0, 0, 0, 0, 0)")) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, username);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Insert the player kits into the player_kits table
            try (PreparedStatement kitStatement = connection.prepareStatement(
                    "INSERT INTO player_kits (uuid, MARKSMAN, SKALD, BOMBARDIER, BERSERKER, WARRIOR, WIZARD, SPEARMAN, KNIGHT) " +
                            "VALUES (?, true, true, false, false, false, false, false, false)")) {
                kitStatement.setString(1, playerUUID.toString());
                kitStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void incrementWins(UUID uuid, int coinsToAdd) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET wins = wins + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();

            // Then, increment the coins
            PreparedStatement coinsStatement = connection.prepareStatement("UPDATE player_stats SET coins = coins + ? WHERE uuid = ?");
            coinsStatement.setInt(1, coinsToAdd); // Set the number of coins to increment
            coinsStatement.setString(2, uuid.toString());
            coinsStatement.executeUpdate();
            coinsStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementKills(UUID uuid, int coinsToAdd) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET kills = kills + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();

            // Then, increment the coins
            PreparedStatement coinsStatement = connection.prepareStatement("UPDATE player_stats SET coins = coins + ? WHERE uuid = ?");
            coinsStatement.setInt(1, coinsToAdd); // Set the number of coins to increment
            coinsStatement.setString(2, uuid.toString());
            coinsStatement.executeUpdate();
            coinsStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDeaths(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementKingKills(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET king_kills = king_kills + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public int getPlayerCoins(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT coins FROM player_stats WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("coins");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if there was an error or the player does not exist
    }

    /**
     * Remove coins from a player's account.
     *
     * @param uuid The player's UUID.
     * @param coinsToRemove The number of coins to remove.
     * @return true if successful, false otherwise.
     */
    public boolean removePlayerCoins(UUID uuid, int coinsToRemove) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE player_stats SET coins = coins - ? WHERE uuid = ? AND coins >= ?")) {
            statement.setInt(1, coinsToRemove);
            statement.setString(2, uuid.toString());
            statement.setInt(3, coinsToRemove); // Ensure they have enough coins

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error
    }

    /**
     * Check if a player has a specific kit.
     *
     * @param uuid The player's UUID.
     * @param kitName The name of the kit to check (e.g., "MARKSMAN", "WARRIOR").
     * @return true if the player has the kit, false otherwise.
     */
    public boolean checkPlayerKit(UUID uuid, String kitName) {
        // Ensure the kit name is valid to avoid SQL injection
        String[] validKits = {"MARKSMAN", "SKALD", "BOMBARDIER", "BERSERKER", "WARRIOR", "WIZARD", "SPEARMAN", "KNIGHT"};
        boolean isValidKit = false;

        for (String kit : validKits) {
            if (kit.equalsIgnoreCase(kitName)) {
                isValidKit = true;
                kitName = kit; // Normalize the case
                break;
            }
        }

        if (!isValidKit) {
            throw new IllegalArgumentException("Invalid kit name: " + kitName);
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT " + kitName + " FROM player_kits WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(kitName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the player does not have the kit or an error occurred
    }

    public void unlockPlayerKit(UUID uuid, KitType kit) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE player_kits SET " + kit.toString() + " = TRUE WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerCoins(UUID uuid, int amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET coins = ? WHERE uuid = ?");
            statement.setInt(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add coins to a player
    public void addPlayerCoins(UUID uuid, int amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET coins = coins + ? WHERE uuid = ?");
            statement.setInt(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
