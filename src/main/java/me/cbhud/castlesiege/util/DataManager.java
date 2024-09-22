package me.cbhud.castlesiege.util;

import me.cbhud.castlesiege.CastleSiege;
import me.cbhud.castlesiege.kits.KitType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.UUID;
import java.util.function.Consumer;

public class DataManager {
    private final CastleSiege plugin;
    private Connection connection;

    public DataManager(CastleSiege plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Create an H2 data source
                    JdbcDataSource dataSource = new JdbcDataSource();
                    dataSource.setURL("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/data");

                    // Connect to H2 database
                    connection = dataSource.getConnection();

                    // Create the tables if they don't exist
                    createTables();

                } catch (SQLException e) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void createTables() {
        new BukkitRunnable() {
            @Override
            public void run() {
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
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        plugin.getLogger().severe("Error creating tables: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
            }
        }.runTaskAsynchronously(plugin);
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
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasData(playerUUID)) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO player_stats (uuid, username, wins, kills, deaths, king_kills, coins) " +
                                    "VALUES (?, ?, 0, 0, 0, 0, 0)")) {
                        statement.setString(1, playerUUID.toString());
                        statement.setString(2, username);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

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
        }.runTaskAsynchronously(plugin);
    }

    public void incrementWins(UUID uuid, int coinsToAdd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET wins = wins + 1 WHERE uuid = ?");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                    statement.close();

                    PreparedStatement coinsStatement = connection.prepareStatement("UPDATE player_stats SET coins = coins + ? WHERE uuid = ?");
                    coinsStatement.setInt(1, coinsToAdd);
                    coinsStatement.setString(2, uuid.toString());
                    coinsStatement.executeUpdate();
                    coinsStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void incrementKills(UUID uuid, int coinsToAdd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET kills = kills + 1 WHERE uuid = ?");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                    statement.close();

                    PreparedStatement coinsStatement = connection.prepareStatement("UPDATE player_stats SET coins = coins + ? WHERE uuid = ?");
                    coinsStatement.setInt(1, coinsToAdd);
                    coinsStatement.setString(2, uuid.toString());
                    coinsStatement.executeUpdate();
                    coinsStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void incrementDeaths(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void incrementKingKills(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET king_kills = king_kills + 1 WHERE uuid = ?");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
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

    public void removePlayerCoins(UUID uuid, int coinsToRemove, Consumer<Boolean> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean success = false;
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player_stats SET coins = coins - ? WHERE uuid = ? AND coins >= ?")) {
                    statement.setInt(1, coinsToRemove);
                    statement.setString(2, uuid.toString());
                    statement.setInt(3, coinsToRemove);

                    int rowsUpdated = statement.executeUpdate();
                    success = rowsUpdated > 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Run the callback with the result on the main thread
                boolean finalSuccess = success;
                Bukkit.getScheduler().runTask(plugin, () -> callback.accept(finalSuccess));
            }
        }.runTaskAsynchronously(plugin);
    }

    public boolean checkPlayerKit(UUID uuid, String kitName) {
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
        return false;
    }

    public void unlockPlayerKit(UUID uuid, KitType kit) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player_kits SET " + kit.toString() + " = TRUE WHERE uuid = ?")) {
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void setPlayerCoins(UUID uuid, int amount) {
        new BukkitRunnable() {
            @Override
            public void run() {
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
        }.runTaskAsynchronously(plugin);
    }

    public void addPlayerCoins(UUID uuid, int amount) {
        new BukkitRunnable() {
            @Override
            public void run() {
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
        }.runTaskAsynchronously(plugin);
    }

    public Connection getConnection() {
        return connection;
    }
}
